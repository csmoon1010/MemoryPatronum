package techtown.org.memorypatronum;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import techtown.org.memorypatronum.PersonalData;
import techtown.org.memorypatronum.R;


public class UsersAdapter3 extends RecyclerView.Adapter<UsersAdapter3.CustomViewHolder> {

    private ArrayList<PersonalData3> mList = null;
    private Activity context = null;


    public UsersAdapter3(Activity context, ArrayList<PersonalData3> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView mind;
        //protected TextView type;
        //protected TextView address;


        public CustomViewHolder(View view) {
            super(view);
            this.mind = (TextView) view.findViewById(R.id.textView_listMIND);
            //this.curName_new = (TextView) view.findViewById(R.id.textView_list_curName_new);
            //this.type = (TextView) view.findViewById(R.id.textView_list_type);
            //this.address = (TextView) view.findViewById(R.id.textView_list_address);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list3, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.mind.setText(mList.get(position).getMember_mind());

        //viewholder.curName_new.setText(mList.get(position).getMember_curName_new());
        //viewholder.type.setText(mList.get(position).getMember_type());
        //viewholder.address.setText(mList.get(position).getMember_address());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

