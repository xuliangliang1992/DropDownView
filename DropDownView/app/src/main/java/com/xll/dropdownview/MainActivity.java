package com.xll.dropdownview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xll.dropdownview.view.DropDownView;


public class MainActivity extends AppCompatActivity {

    DropDownView dropDownView;
    private String[] FirstTitle = {"a1", "b1", "c1", "d1"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropDownView = (DropDownView) findViewById(R.id.dropdownview);
        dropDownView.setmContext(this);
        dropDownView.setLastList(FirstTitle);

        dropDownView.setOnDropDownItemClickListener(new DropDownView.OnDropDownItemClickListener() {
            @Override
            public void onDropDownItemClick(String selectedDropDownChildItem, int clickDropDownNum) {
                dropDownView.setTitle(selectedDropDownChildItem,clickDropDownNum);
            }
        });

        dropDownView.setOnDropDownClickListener(new DropDownView.OnDropDownClickListener() {
            @Override
            public void onDropDownClick(int clickDropDownNum) {
                switch (clickDropDownNum) {
                    case 0:
                        dropDownView.setStrs(Constant.A,clickDropDownNum);
                        break;
                    case 1:
                        dropDownView.setStrs(Constant.B,clickDropDownNum);
                        break;
                    case 2:
                        dropDownView.setStrs(Constant.C,clickDropDownNum);
                        break;
                    case 3:
                        dropDownView.setStrs(Constant.D,clickDropDownNum);
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
