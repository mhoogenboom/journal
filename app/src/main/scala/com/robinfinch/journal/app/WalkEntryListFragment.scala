package com.robinfinch.journal.app;

import android.app.{Activity, LoaderManager}
import android.content.{AsyncQueryHandler, ContentValues, Context, CursorLoader, Loader}
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{CursorAdapter, TextView}
import com.robinfinch.journal.app.persistence.{JournalEntryContract, WalkEntryContract}
import com.robinfinch.journal.app.ui.ExpandableListFragment
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter
import com.robinfinch.journal.app.util.ImplicitConversions._
import com.robinfinch.journal.app.util.{Formatter, Utils}
import com.robinfinch.journal.app.util.Utils.alias
import com.robinfinch.journal.domain.WalkEntry

/**
 * List of walk entries fragment.
 *
 * @author Mark Hoogenboom
 */
object WalkEntryListFragment {
  val LOAD_WALK_ENTRIES = 1
  val INSERT_WALK_ENTRY = 2

  def newInstance() = {
    val fragment = new WalkEntryListFragment()

    val args = new Bundle()
    fragment.setArguments(args)

    fragment
  }

  trait Parent {
    def onWalkEntryItemSelected(uri: Uri)
  }
}

class WalkEntryListFragment extends ExpandableListFragment {

  var loaderCallbacks: LoaderManager.LoaderCallbacks[Cursor] = null

  var queryHandler: AsyncQueryHandler = null

  var parent: WalkEntryListFragment.Parent = null

  override def getHeaderResId() = R.string.walkentries

  override def getAddButtonResId() = R.string.entity_add

  override def onAttach(activity: Activity) {
        super.onAttach(activity)
        parent = activity.asInstanceOf[WalkEntryListFragment.Parent]
  }

  override def onCreate(savedInstanceState : Bundle) {
        super.onCreate(savedInstanceState)

        adapter = new JournalEntryListAdapter(new CursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

            override def newView(context : Context, cursor: Cursor, parent: ViewGroup) = {
                val view = LayoutInflater.from(context).inflate(R.layout.walkentry_list_item, parent, false)

                val viewHolder = new ViewHolder(view)
                view.setTag(viewHolder)

                view
            }

            override def bindView(view: View, context: Context, cursor: Cursor) {
                val walkEntry = WalkEntry.from(cursor)

                val viewHolder = view.getTag().asInstanceOf[ViewHolder]
                viewHolder.bind(walkEntry)
            }
        }, alias(WalkEntryContract.NAME, JournalEntryContract.COL_DAY_OF_ENTRY))
    }

    override def onActivityCreated(savedInstanceState: Bundle) {
        super.onActivityCreated(savedInstanceState)

        loaderCallbacks = new LoaderManager.LoaderCallbacks[Cursor]() {
            override def onCreateLoader(id: Int, args: Bundle) : Loader[Cursor] =
                new CursorLoader(
                        getActivity(),
                        WalkEntryContract.DIR_URI_TYPE.uri(getActivity()),
                        WalkEntryContract.COLS, null, null, JournalEntryContract.COL_DAY_OF_ENTRY + " ASC")

            override def onLoadFinished(loader: Loader[Cursor], cursor: Cursor) {
                adapter.swapCursor(cursor)

                if (adapter.getGroupCount() > 0) {
                    list.expandGroup(adapter.getGroupCount() - 1)
                }
            }

            override def onLoaderReset(loader: Loader[Cursor]) {
                adapter.swapCursor(null)
            }
        }

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            override def onInsertComplete(token: Int, cookie: Object, uri: Uri) {
                parent.onWalkEntryItemSelected(uri)
            }
        }

        getLoaderManager().initLoader(WalkEntryListFragment.LOAD_WALK_ENTRIES, null, loaderCallbacks)
    }

    override def add() {
        val initialValues = new ContentValues()
        initialValues.put(JournalEntryContract.COL_DAY_OF_ENTRY, Utils.getDefaultDayOfEntry(getActivity()).asInstanceOf[java.lang.Long])
        initialValues.put(WalkEntryContract.COL_LOCATION, "")

        queryHandler.startInsert(WalkEntryListFragment.INSERT_WALK_ENTRY, null, WalkEntryContract.DIR_URI_TYPE.uri(getActivity()), initialValues)
    }

    override def select(id : Long) {
        parent.onWalkEntryItemSelected(WalkEntryContract.ITEM_URI_TYPE.uri(getActivity(), id))
    }

    override def onDetach() {
        parent = null
        super.onDetach()
    }

    class ViewHolder(val view: View) {

      val dayOfEntryView = view.findView[TextView](R.id.walkentry_dayofwalk)

      val descriptionView = view.findView[TextView](R.id.walkentry_description)

      def bind(entry: WalkEntry) {
          dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()))
          descriptionView.setText(Formatter.formatWalkDescription(entry.getLocation()))
      }
    }
}
