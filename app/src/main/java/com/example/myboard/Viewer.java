package com.example.myboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Viewer extends AppCompatActivity {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference reference_serial;
    DatabaseReference reference_number;
    DatabaseReference refrence1;
    DatabaseReference reference2;
    String mess,dt,combo,sral;
    private ProgressDialog loadingBar;
    private LinearLayout parentLinearLayout;
    List<String> vec=new ArrayList<String>();
    int varr=1;
    boolean dn;
    ListView lv;
    Button submit,search_btn;
    EditText serial,seach;
    TextView put,top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        top=(TextView)findViewById(R.id.tt);
        submit=(Button)findViewById(R.id.submit);
        serial=(EditText)findViewById(R.id.serial);
        put=(TextView)findViewById(R.id.put);
        parentLinearLayout=(LinearLayout) findViewById(R.id.parent_layout1);
        lv = (ListView) findViewById(R.id.myhistry1);
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference();
        loadingBar = new ProgressDialog(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sral=serial.getText().toString();
                loadingBar.setTitle("Checking Serial Number");
                loadingBar.setMessage("Please wait, while we are checking.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(sral).exists())
                        {
                            loadingBar.dismiss();
                            parentLinearLayout.removeView(top);
                            parentLinearLayout.removeView(submit);
                            parentLinearLayout.removeView(put);
                            parentLinearLayout.removeView(serial);
                            loadingBar.setTitle("Checking");
                            loadingBar.setMessage("Please wait, while we are checking and taking all datas.");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();
                                    while (snapshot.child(sral).child(String.valueOf(varr)).exists()) {
                                        mess=snapshot.child(sral).child(String.valueOf(varr)).child("message").getValue(String.class);
                                        dt=snapshot.child(sral).child(String.valueOf(varr)).child("date_time").getValue(String.class);
                                        combo=mess+"\n"+"Time :-"+dt;
                                        vec.add(combo);
                                        varr++;
                                    }
                                    final int sz=vec.size();
                                    final String data[]=new String[sz];
                                    for(int i=0;i<sz;i++) {data[i]=vec.get(i);}
                                    vec.clear();
                                    loadingBar.dismiss();
                                    lv.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.lview, R.id.mytext, data));

                        }
                        else
                        {
                            Toast.makeText(Viewer.this,"Sorry, This Serial Number doesn't exist!",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Viewer.this,"Sorry, Error!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    String [] search(String[]data,int sz,String icon)
    {
        int count=0;
        for(int i=0;i<sz;i++)
        {
            if(data[i].contains(icon))
            {
                count++;
            }
        }
        String ans[]=new String[count];
        int cont=0;
        for(int i=0;i<sz;i++)
        {
            if(data[i].contains(icon))
            {
               ans[cont]=data[i];
               cont++;
            }
        }
        return ans;
    }
}