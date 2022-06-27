package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokerchips.R;

import java.util.ArrayList;

import model.Player;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersViewHolder>{

    private final ArrayList<Player> localDataSet;
    private final Context parentContext;

    public PlayersAdapter(Context current, ArrayList<Player> dataSet) {
        parentContext = current;
        localDataSet = dataSet;
    }
    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);

        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersViewHolder holder, int position) {
        holder.getuser_name().setText(
                localDataSet.get(position).getUsername());

        holder.getuser_chips().setText(
                localDataSet.get(position).getUserChips());


    }

    @Override
    public int getItemCount() {
        if (localDataSet != null) {
            return localDataSet.size();
        }
        return 0;
    }
}
