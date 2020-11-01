package com.example.nutrobud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.mlkit.vision.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScanResult extends AppCompatActivity {

    public List<String> blocks= new ArrayList<>();
    public String scannedText;

    public ScanResult(){}
    public ScanResult(List<String> blocks, String scannedText){
        this.blocks = blocks;
        this.scannedText = scannedText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        TextView scannedTextElement = findViewById(R.id.scannedText);
        System.out.println("Created ScanResult");
        for(int i=0; i<blocks.size();i++){
            scannedTextElement.append("plus"+blocks.get(i));
            System.out.println("Here is the text: "+ blocks.get(i));
        }
    }

    public void setScannedText(List<Text.TextBlock> textBlocks) {
        if(blocks!=null){
            blocks.clear();
        }
        else{
            for(int i=0; i<textBlocks.size();i++){
                blocks.add(textBlocks.get(i).getText());
            }
        }

    }

    public String getScannedText() {
        return scannedText;
    }
}