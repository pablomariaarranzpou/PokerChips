package adapters;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.Player;

public class DatabaseAdapter extends Activity {

    public static final String TAG = "DatabaseAdapter";

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static plInterface listener;
    public static DatabaseAdapter databaseAdapter;


    public interface plInterface{
        void setCollection(ArrayList<Player> pl);
        void setToast(String s);
    }



    public DatabaseAdapter(plInterface listener) {
        this.listener = listener;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
        initFirebase();
    }


    public void initFirebase() {

        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                listener.setToast("Authentication successful.");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                listener.setToast("Authentication failed.");

                            }
                        }
                    });
    }


    public void getRoomPlayers(String roomID){
        DatabaseAdapter.db.collection("Curso").document(roomID)
                .collection("Player").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { ;
                            ArrayList<Player> acc = new ArrayList<Player>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d("PLAYER", document.getData().toString());
                                    acc.add(new Player(document.getString("player_name"), Math.toIntExact(document.getLong("player_chips"))));
                                }
                            }
                            listener.setCollection(acc);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}