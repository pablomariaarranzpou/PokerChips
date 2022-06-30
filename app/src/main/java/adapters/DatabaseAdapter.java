package adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        Log.d("LLEGA", "LLEGA A METODO");
        db.collection("Room").document(roomID).collection("Player")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d("cambios", "HAY CAMBIOS");
                        ArrayList<Player> acc = new ArrayList<Player>();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            if (document.exists()) {
                                Log.d("PLAYER", document.getData().toString());
                                acc.add(new Player(document.getString("player_name"), Math.toIntExact(document.getLong("player_chips")), document.getString("player_id")));
                            }
                        }
                        listener.setCollection(acc);

                    }
                });


    }



}
