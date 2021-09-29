package com.example.dmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView listview;
public String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            listview = findViewById(R.id.listview);
            runTimePermission();

    }

public void runTimePermission(){

    Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                display();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            })
            .check();
}
public ArrayList<File> fetchSongs(File file)
{

        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if (songs != null){

            for (File myfile: songs)
            {
                if (myfile.isDirectory() && !myfile.isHidden())
                {
                    arrayList.addAll(fetchSongs(myfile));
                }
                else {
                    if (myfile.getName().endsWith(".mp3") || myfile.getName().endsWith(".wav") || myfile.getName().endsWith(".ogg"))
                    {
                        arrayList.add(myfile);
                    }
                }
            }
        }

        return arrayList;
    }
void display()
{
       final ArrayList<File> mysongs = fetchSongs(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++)
        {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","").replace(".ogg","");
        }
    ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listview.setAdapter(myadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songname = listview.getItemAtPosition(i).toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("mysongs",mysongs).putExtra("songname",songname).putExtra("position",i));
            }
        });
}

}