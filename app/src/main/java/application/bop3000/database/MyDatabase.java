package application.bop3000.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.Executors;

@Database(entities = {User.class, FAQ.class, Post.class, PostOffice.class, Subscription.class, Payment.class}, version=17)
public abstract class MyDatabase extends RoomDatabase{

    // Database name to be used
    public static final String DBNAME = "Knittersbox";
    // instance variable
    private static MyDatabase INSTANCE;

    // **********//
    // Declare your data access objects *DAO* as abstract
    public abstract KnittersboxDao getKnittersboxDao();
    // **********//

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, DBNAME)
                            // Wipes and rebuilds instead of migrating // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getDatabase(context).getKnittersboxDao().registerFaq(FAQ.populateFAQData());
                                            getDatabase(context).getKnittersboxDao().registerSubscription(Subscription.populateSubscriptionData());
                                            getDatabase(context).getKnittersboxDao().registerPostOffice(PostOffice.populatePostOfficeData());
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
