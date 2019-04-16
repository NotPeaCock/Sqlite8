package com.trungse.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.trungse.model.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    String DATABASE_NAME = "db_Contact.s3db";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;
    ListView lvContact;
    ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        addControls();
        hienThiToanBoSanPham();
    }

    private void hienThiToanBoSanPham() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        //Cursor cursor = database.rawQuery("select* from Contact", null);
        Cursor cursor = database.query("Contact",null,"Ma=? or Ma=?", new String[] {"1","4"}, null, null, null, null);
        adapter.clear();
        while(cursor.moveToNext()){
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phone = cursor.getString(2);
            Contact contact = new Contact(ma, ten, phone);
            adapter.add(contact);
        }
        cursor.close();
    }

    private void addControls() {
        lvContact = findViewById(R.id.lv_contact);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
        lvContact.setAdapter(adapter);

    }

    private void processCopy() {
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                copyDatabaseFromAssets();
                Toast.makeText(this, "Sao chep CSDL Sqlite thanh cong!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Log.e("Loi", ex.toString());
        }
    }

    private String getDatabasePath(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }

    private void copyDatabaseFromAssets() {
        try{
            InputStream myInput = getAssets().open(DATABASE_NAME, MODE_PRIVATE);
            File f=new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists())
                f.mkdir();
            String outFileName = getDatabasePath();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte []buffer = new byte[1024];
            int length;
            while((length=myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        }catch(Exception ex){
         Log.e("Loi", ex.toString());
        }
    }
}
