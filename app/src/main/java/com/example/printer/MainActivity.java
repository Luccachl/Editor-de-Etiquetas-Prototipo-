package com.example.printer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FrameLayout drawingArea;
    private EditText editTextWidth;
    private EditText editTextHeight;
    private FloatingActionButton floatingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("MainActivity", "onCreate called");
        initializeViews();
    }

    private void initializeViews() {
        drawingArea = findViewById(R.id.drawingArea);
        editTextWidth = findViewById(R.id.width);
        editTextHeight = findViewById(R.id.height);
        floatingButton = findViewById(R.id.floatingActionButton3);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não necessário
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDrawingAreaDimensions();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não necessário
            }
        };

        editTextWidth.addTextChangedListener(textWatcher);
        editTextHeight.addTextChangedListener(textWatcher);

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Temporariamente desabilitar TextWatcher para evitar loops
                    editTextWidth.removeTextChangedListener(textWatcher);
                    editTextHeight.removeTextChangedListener(textWatcher);

                    ((EditText) v).setText("");

                    // Reabilitar TextWatcher
                    editTextWidth.addTextChangedListener(textWatcher);
                    editTextHeight.addTextChangedListener(textWatcher);
                }
            }
        };

        editTextWidth.setOnFocusChangeListener(onFocusChangeListener);
        editTextHeight.setOnFocusChangeListener(onFocusChangeListener);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectionWindow();
            }
        });
    }
//preciso colocar limite de tamanho
    private void updateDrawingAreaDimensions() {
        String widthText = editTextWidth.getText().toString();
        String heightText = editTextHeight.getText().toString();

        int width = drawingArea.getLayoutParams().width; // Pegar valor atual da largura
        int height = drawingArea.getLayoutParams().height; // Pegar valor atual da altura

        try {
            if (!widthText.isEmpty()) {
                width = Integer.parseInt(widthText);
                if (width < 0) width = 0; // Evitar valores negativos
            }
        } catch (NumberFormatException e) {
            Log.d("MainActivity", "Invalid width format");
        }

        try {
            if (!heightText.isEmpty()) {
                height = Integer.parseInt(heightText);
                if (height < 0) height = 0; // Evitar valores negativos
            }
        } catch (NumberFormatException e) {
            Log.d("MainActivity", "Invalid height format");
        }

        Log.d("MainActivity", "Updating dimensions to width: " + width + ", height: " + height);

        ViewGroup.LayoutParams params = drawingArea.getLayoutParams();
        params.width = width;
        params.height = height;
        drawingArea.setLayoutParams(params);

        Log.d("MainActivity", "Dimensions updated");
    }
    private void openSelectionWindow(){

        String [] options = {
                "Texto", "Imagem", "QR Code", "Código de Barras"
        };

        new android.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Selecione uma opção")
                .setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                    Toast.makeText(MainActivity.this, options[which], Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}
