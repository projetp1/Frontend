package com.github.projetp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.AbstractListModel;

/**
 * The Class ListModel.
 *
 * @param <F> the generic type
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ListModel<F> extends AbstractListModel
{

	/** The list. */
	ArrayList<F> list = new ArrayList<F>();

	/**
	 * Instantiates a new list model.
	 */
	public ListModel()
	{
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize()
	{
		return list.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int index)
	{
		return list.get(index);
	}

	/**
	 * Add a new element to the list
	 *
	 * @param element The new element
	 */
	public void setElement(F element)
	{
		list.add(element);
		super.fireIntervalAdded(this, 0, list.size() - 1);
	}

	/**
	 * Removes an element from the list
	 *
	 * @param element the element to remove
	 */
	public void removeElement(F element)
	{
		int index = list.indexOf(element);
		list.remove(element);
		super.fireIntervalRemoved(this, index, index);
	}

	/**
	 * Removes the element at a specified index
	 *
	 * @param index the index
	 */
	public void removeElement(int index)
	{
		list.remove(index);
		super.fireIntervalRemoved(this, 0, list.size() - 1);
	}

	/**
	 * Removes all the elements
	 */
	public void removeAll()
	{
		super.fireIntervalRemoved(this, 0, list.size());
		list.clear();

	}

	/**
	 * Sets the new list.
	 *
	 * @param keySet the new list
	 */
	@SuppressWarnings("unchecked")
	public void setNewList(Set<String> keySet)
	{
		list.clear();
		list.addAll((Collection<? extends F>) keySet);
		super.fireIntervalAdded(this, 0, list.size() - 1);
	}

	/**
	 * Converts the list to an Array
	 *
	 * @return An array of String
	 */
	public String[] toArray()
	{
		return list.toArray(new String[] {});
	}

}
