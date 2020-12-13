package com.example.myboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextView tx,tx1;
    ListView lv;
    String[] data;
    Button prss;
    int pass=12345;
    Button usr,vwr;
    private LinearLayout parentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference();
        final Mydb mfo = new Mydb(getApplicationContext());

        parentLinearLayout=(LinearLayout) findViewById(R.id.parent_layout);
        lv = (ListView) findViewById(R.id.myhistry);

        usr=(Button)findViewById(R.id.user);
        vwr=(Button)findViewById(R.id.vwr);
        tx=(TextView)findViewById(R.id.txt);
        tx1=(TextView) findViewById(R.id.tt);
        tx1.setVisibility(View.INVISIBLE);

        usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mfo.my_data().equals(""))
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            while(snapshot.child(String.valueOf(pass)).exists()) {pass++;}
                            Toast.makeText(MainActivity.this,"Your serial Number is "+pass,Toast.LENGTH_SHORT).show();
                            DataTemp dt = new DataTemp(String.valueOf(pass),String.valueOf(pass));
                            mfo.addingDataToTable(dt);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this,"Sorry, Error!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                parentLinearLayout.removeView(usr);
                parentLinearLayout.removeView(vwr);
                parentLinearLayout.removeView(tx);
                tx1.setVisibility(View.VISIBLE);
                /// think about constructor of MyDBFunctions getting context parameter
                final MyDBFunctions mf = new MyDBFunctions(getApplicationContext());
                /// receiving database's all datas from method of my_data()
                data = mf.my_data();

                lv.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.lview, R.id.mytext, data));

            }
        });

        vwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Viewer.class);
                startActivity(i);
                finish();
            }
        });

    }
}