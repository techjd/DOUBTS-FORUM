package jee.techjd.jeeebooks;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCache extends android.app.Application   {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
