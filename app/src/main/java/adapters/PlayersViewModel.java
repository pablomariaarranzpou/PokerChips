package adapters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import model.Player;

public class PlayersViewModel extends AndroidViewModel implements DatabaseAdapter.plInterface {
    private final MutableLiveData<ArrayList<Player>> mPlayers;
    private final MutableLiveData<String> mToast;
    private final Application mAplicattion;

    public PlayersViewModel(Application application, String roomID) {
        super(application);
        mPlayers = new MutableLiveData<>();
        mToast = new MutableLiveData<>();
        mAplicattion = application;
        DatabaseAdapter da = new DatabaseAdapter(this);
        da.getRoomPlayers(roomID);
    }
    public LiveData<ArrayList<Player>> getPlayersCards(){
        return mPlayers;
    }

    public Player getPlayerCard(int idx){
        return mPlayers.getValue().get(idx);
    }

    public LiveData<String> getToast(){
        return mToast;
    }

    @Override
    public void setCollection(ArrayList<Player> pl) {
        this.mPlayers.setValue(pl);
    }

    @Override
    public void setToast(String s) {

    }
}