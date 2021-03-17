package application.bop3000.FAQ;

import androidx.appcompat.app.AppCompatActivity;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.Subscription.Subscription_change;
import application.bop3000.database.FAQ;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Subscription;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class faq extends AppCompatActivity {

    private List<String> itemSet;
    private MyDatabase mDb;
    private ExpandableListView expandableListView;
    private List<FAQ> faqList;
    private List<String> listGroup;
    private HashMap<String,List<String>> listItem;
    private MainAdapter adapter;
    private Integer size;
    private Integer count;
    private Integer increment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        mDb = MyDatabase.getDatabase(getApplicationContext());
        faqList = new ArrayList<>();
        itemSet = new ArrayList<>();
        expandableListView = findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        getFaq();

    }


    private void getFaq() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                faqList = mDb.getKnittersboxDao().faqList();
                size = faqList.size();

                for (count = 0; count < size; count++) {
                    FAQ faq = faqList.get(count);
                    String faqSp = faq.getQuestion();
                    String faqAn = faq.getAnswer();
                    listGroup.add(faqSp);
                    itemSet.add(faqAn);
                }
                showFaq();
            }
        });
    }
    private void showFaq() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                increment = 0;
                for (count = 0; count < size; count++) {
                    listItem.put(listGroup.get(increment), Collections.singletonList(itemSet.get(increment)));
                    ++increment;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}