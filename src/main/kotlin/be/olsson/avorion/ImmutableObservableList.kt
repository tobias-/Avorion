package be.olsson.avorion

import javafx.beans.InvalidationListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.util.Collections

open class ImmutableObservableList<E>(list: List<E>) : ObservableList<E>, MutableList<E> by Collections.unmodifiableList(list) {
    override fun addListener(listener: InvalidationListener) {
    }

    override fun removeListener(listener: InvalidationListener) {
    }

    override fun addListener(listener: ListChangeListener<in E>) {
    }

    override fun removeListener(listener: ListChangeListener<in E>) {
    }

    override fun addAll(vararg elements: E): Boolean = throw UnsupportedOperationException("List is read only")
    override fun remove(from: Int, to: Int) = throw UnsupportedOperationException("List is read only")
    override fun removeAll(vararg elements: E): Boolean = throw UnsupportedOperationException("List is read only")
    override fun retainAll(vararg elements: E): Boolean = throw UnsupportedOperationException("List is read only")
    override fun setAll(vararg elements: E): Boolean = throw UnsupportedOperationException("List is read only")
    override fun setAll(col: MutableCollection<out E>?): Boolean = throw UnsupportedOperationException("List is read only")
}
