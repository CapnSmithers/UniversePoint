package edu.mines.msmith1.universepoint.dto;

import java.util.List;

import edu.mines.msmith1.universepoint.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * An {@link ArrayAdapter} that handles DTO objects generically by populating a {@link TextView}
 * with {@link BaseDTO#toString()}.
 * @author vanxrice
 */
public class BaseDTOArrayAdapter extends ArrayAdapter<BaseDTO> {
	private List<BaseDTO> mValues;
	private int mResource; // specifies the view to use when instantiating views

	public BaseDTOArrayAdapter(Context context, int resource, List<BaseDTO> values) {
		super(context, resource, values);
		
		this.mValues = values;
		this.mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(mResource, parent, false);
		} else {
			rowView = convertView;
		}
		
		BaseDTO item = mValues.get(position);
		if (item != null) {
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			if (textView != null) {
				textView.setText(item.toString());
			}
		}
		
		return rowView;
	}
}
