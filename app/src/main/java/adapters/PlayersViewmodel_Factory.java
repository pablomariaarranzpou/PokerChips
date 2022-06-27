package adapters;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PlayersViewmodel_Factory implements ViewModelProvider.Factory{
        private Application mApplication;
        private String mParam;


        public PlayersViewmodel_Factory(Application application, String param) {
            mApplication = application;
            mParam = param;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PlayersViewModel(mApplication, mParam);
        }
    }

