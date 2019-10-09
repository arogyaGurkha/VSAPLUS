package com.example.vsaplus;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NormalCommunityFragment extends Fragment {
    public static NormalCommunityFragment newInstance(){
        return new NormalCommunityFragment();
    }
    ImageButton exit;
    RecyclerView normalView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("community");
    FirebaseRecyclerAdapter<PostModel,PostViewHolder2> adapter2;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_community_list,container,false);
        normalView = view.findViewById(R.id.communityList);
        exit = view.findViewById(R.id.exit_post);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).loadFragment(CommunityFragment.newInstance());
            }
        });
        normalView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = myRef.child("post").orderByKey();
        setnormalRecyclerView(query);
        return view;
    }
    public void setnormalRecyclerView(Query query) {
        FirebaseRecyclerOptions<PostModel> options = new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(query,PostModel.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<PostModel, PostViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder2 postViewHolder, int position, @NonNull PostModel postModel) {
                postViewHolder.setPostname(postModel.gettitle(),postModel.getUserName(),postModel.getContent(),postModel.getUserUid(),postModel.getLike(),postModel.getReply(),postModel.getPostnum());

                Log.d("Start", "posttitle data: " +  postModel.gettitle() );
            }

            @NonNull
            @Override
            public PostViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user_board, parent, false);
                return new PostViewHolder2(view);
            }
        };
        normalView.setAdapter(adapter2);
        adapter2.startListening();
    }
    public class PostViewHolder2 extends RecyclerView.ViewHolder {

        View mView;

        PostViewHolder2(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setPostname(final String title,final String username,final String content,final String UserUid,final int like,final int reply,final int postnum) {
            TextView nameView = mView.findViewById(R.id.title);//fix
            TextView userView = mView.findViewById(R.id.user_ID);//fix
            TextView LikeView = mView.findViewById(R.id.likeCount);
            TextView ReplyView = mView.findViewById(R.id.commentCount);
            LinearLayout clickview = mView.findViewById(R.id.clickview);

            nameView.setText(title);
            userView.setText(username);
            LikeView.setText(like+"");
            ReplyView.setText(reply+"");
            clickview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new PostViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title",title);
                    bundle.putString("content", content);
                    bundle.putInt("Like",like);
                    bundle.putString("UserName",username);
                    bundle.putInt("postnum",postnum);
                    bundle.putString("userUid",UserUid);
                    bundle.putInt("reply",reply);
                    fragment.setArguments(bundle);
                    ((MainActivity)getActivity()).loadFragment(fragment);
                }
            });

        }

    }}
