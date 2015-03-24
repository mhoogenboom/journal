
package com.robinfinch.journal.app.util

import android.app.FragmentManager
import android.view.View

object ImplicitConversions {

  implicit def view2ViewFinder(view: View) =
    new ViewFinder(view)

  implicit def fragmentManager2FragmentFinder(fragmentManager: FragmentManager) =
    new FragmentFinder(fragmentManager)

  implicit def function2ActionListener(f: View => Unit) =
    new View.OnClickListener() {
      override def onClick(view: View) {
        f(view)
      }
    }
}

class ViewFinder(view: View) {
  def findView[T](id: Int): T = view.findViewById(id).asInstanceOf[T]
}

class FragmentFinder(fragmentManager: FragmentManager) {
  def findFragment[T](id: Int): T = fragmentManager.findFragmentById(id).asInstanceOf[T]
}