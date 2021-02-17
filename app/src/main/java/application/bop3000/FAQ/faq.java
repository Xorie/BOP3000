package application.bop3000.FAQ;

import androidx.appcompat.app.AppCompatActivity;

import application.bop3000.R;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class faq extends AppCompatActivity {
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listItem;
    MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        expandableListView = findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        initListData();
    }

    private void initListData() {
        listGroup.add(getString(R.string.faq_group1));
        listGroup.add(getString(R.string.faq_group2));
        listGroup.add(getString(R.string.faq_group3));
        listGroup.add(getString(R.string.faq_group4));
        listGroup.add(getString(R.string.faq_group5));

        String[] array;
        List<String> list = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq_group1);
        for (String item : array) {
            list.add(item);
        }

        List<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq_group2);
        for (String item : array) {
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq_group3);
        for (String item : array) {
            list3.add(item);
        }

        List<String> list4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq_group4);
        for (String item : array) {
            list4.add(item);
        }

        List<String> list5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq_group5);
        for (String item : array) {
            list5.add(item);
        }

        listItem.put(listGroup.get(0), list);
        listItem.put(listGroup.get(1), list2);
        listItem.put(listGroup.get(2), list3);
        listItem.put(listGroup.get(3), list4);
        listItem.put(listGroup.get(4), list5);
        adapter.notifyDataSetChanged();
    }
}