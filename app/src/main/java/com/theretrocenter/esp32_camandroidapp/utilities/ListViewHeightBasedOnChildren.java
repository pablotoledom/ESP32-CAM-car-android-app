package com.theretrocenter.esp32_camandroidapp.utilities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewHeightBasedOnChildren {

    // Constructor
    public ListViewHeightBasedOnChildren() {

    }

    public void setHeight(ListView listView) {
        // Get listview adapter
        ListAdapter listAdapter = listView.getAdapter();

        // Validate adapter
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        // Determine the height based on counted items
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        // Change parameters
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params) .setMargins (10, 10, 10, 10);
        listView.setLayoutParams(params);
    }
}
