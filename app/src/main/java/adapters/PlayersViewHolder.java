package adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokerchips.R;

public class PlayersViewHolder extends RecyclerView.ViewHolder{


    private final TextView user_name;
    private final TextView user_chips;

    public PlayersViewHolder(@NonNull View itemView) {
        super(itemView);
        user_name = (TextView) itemView.findViewById(R.id.user_name_item);
        user_chips = (TextView) itemView.findViewById(R.id.user_chips_item);
    }

    public TextView getuser_name() {
        return user_name;
    }

    public TextView getuser_chips() {
        return user_chips;
    }
}