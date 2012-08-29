package com.github.projetp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.AbstractListModel;

@SuppressWarnings({ "serial", "rawtypes" })
public class ListModel<F> extends AbstractListModel
{

	ArrayList<F> list = new ArrayList<F>();

	public ListModel()
	{
	}

	@Override
	public int getSize()
	{
		return list.size();
	}

	@Override
	public Object getElementAt(int index)
	{
		return list.get(index);
	}

	public void setElement(F element)
	{
		list.add(element);
		super.fireIntervalAdded(this, 0, list.size() - 1);
	}

	public void removeElement(F element)
	{
		int index = list.indexOf(element);
		list.remove(element);
		super.fireIntervalRemoved(this, index, index);
	}

	public void removeElement(int index)
	{
		list.remove(index);
		super.fireIntervalRemoved(this, 0, list.size() - 1);
	}

	public void removeAll()
	{
		super.fireIntervalRemoved(this, 0, list.size());
		list.clear();

	}

	@SuppressWarnings("unchecked")
	public void setNewList(Set<String> keySet)
	{
		list.clear();
		list.addAll((Collection<? extends F>) keySet);
		super.fireIntervalAdded(this, 0, list.size() - 1);
	}

	public String[] toArray()
	{
		return list.toArray(new String[] {});
	}

}
