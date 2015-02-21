package com.robinfinch.journal.app;

import android.app.Activity
import android.app.LoaderManager
import android.content.AsyncQueryHandler
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText

import com.robinfinch.journal.app.persistence.WalkEntryContract
import com.robinfinch.journal.app.ui.DetailsFragment
import com.robinfinch.journal.app.util.Formatter
import com.robinfinch.journal.app.util.Parser
import com.robinfinch.journal.domain.WalkEntry

import com.robinfinch.journal.app.util.Constants.ARG_URI
import com.robinfinch.journal.app.util.ImplicitConversions._

/**
 * Walk entry details fragment.
 *
 * @author Mark Hoogenboom
 */
object WalkEntryFragment {
  val LOAD_WALK_ENTRY = 1
  val UPDATE_WALK_ENTRY = 2

  def newInstance(uri: Uri) = {
    val fragment = new WalkEntryFragment()

    val args = new Bundle();
    args.putParcelable(ARG_URI, uri);
    fragment.setArguments(args);

    fragment;
  }

  trait Parent {
    def onWalkEntryDeleted()
  }
}

class WalkEntryFragment extends DetailsFragment[WalkEntry] {

  protected var dayOfEntryView: EditText = null

  protected var locationView: EditText = null

  private var parent: WalkEntryFragment.Parent = null

  override def onAttach(activity: Activity) {
    super.onAttach(activity)
    parent = activity.asInstanceOf[WalkEntryFragment.Parent]
  }

  override def getLayoutResId() = R.layout.walkentry_fragment

  override def onViewCreated(view: View, savedInstanceState: Bundle) {
    super.onViewCreated(view, savedInstanceState)

    dayOfEntryView = view.findView[EditText](R.id.walkentry_dayofwalk)

    locationView = view.findView[EditText](R.id.walkentry_location)
  }

  override def onActivityCreated(savedInstanceState: Bundle) {
    super.onActivityCreated(savedInstanceState)

    loaderCallbacks = new LoaderManager.LoaderCallbacks[Cursor]() {
      override def onCreateLoader(id: Int, args: Bundle) : Loader[Cursor] = {
        val uri : Uri = getArguments ().getParcelable(ARG_URI)
        new CursorLoader (
          getActivity (),
          uri,
          WalkEntryContract.COLS, null, null, null)
      }

      override def onLoadFinished(loader: Loader[Cursor], cursor: Cursor) {
        if (cursor.moveToFirst()) {
          onWalkEntryLoaded(cursor)
        } else {
          onWalkEntryReset()
        }
      }

      override def onLoaderReset(loader: Loader[Cursor]) {
         onWalkEntryReset()
      }
    }

    queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

      override def onDeleteComplete(token: Int, cookie: Object, result: Int) {
        parent.onWalkEntryDeleted();
      }
    }

    getLoaderManager().initLoader(WalkEntryFragment.LOAD_WALK_ENTRY, null, loaderCallbacks);
  }

  private def onWalkEntryLoaded(cursor: Cursor) {
    entity = WalkEntry.from(cursor)

    val dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry())
    dayOfEntryView.setText(dayOfEntry)

    val location = entity.getLocation()
    locationView.setText(location)

    setShareText(entity.toShareString())
  }

  private def onWalkEntryReset() {
    entity = null
  }

  override def update() {
    if (entity != null) {
      entity.resetChanged()

      val dayOfEntry = Parser.parseDay(dayOfEntryView.getText())
      entity.setDayOfEntry(dayOfEntry)

      val location = Parser.parseText(locationView.getText())
      entity.setLocation(location)

      if (entity.hasChanged()) {
        val uri : Uri = getArguments().getParcelable(ARG_URI)

        val values = entity.toValues()
        queryHandler.startUpdate(WalkEntryFragment.UPDATE_WALK_ENTRY, null, uri, values, null, null)
      }
    }
  }

  override def onDetach() {
    parent = null
    super.onDetach()
  }
}