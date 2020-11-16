package com.example.fypcommunicationtool;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartAssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartAssessmentFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartAssessmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartAssessmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartAssessmentFragment newInstance(String param1, String param2) {
        StartAssessmentFragment fragment = new StartAssessmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_assessment, container, false);

        CardView level1 = view.findViewById(R.id.level1);
        CardView level2 = view.findViewById(R.id.level2);
        CardView level3 = view.findViewById(R.id.level3);

        level1.setOnClickListener(this);
        level2.setOnClickListener(this);
        level3.setOnClickListener(this);

        return view;
    }

    //@Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
//            case R.id.level1 : i = new Intent(this, assessment_instructions.class);startActivity(i); break;
//            case R.id.level2 : i = new Intent(this, StartAssessment2.class);startActivity(i);; break;
//            case R.id.level3 : i = new Intent(this, StartAssessment2.class);startActivity(i); break;
            default:break;
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Select Level");
    }
}