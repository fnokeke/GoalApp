package io.smalldata.frangoal;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareEnv();
        activateSaveButton();
    }

    private void prepareEnv() {
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePrompt();
        populateDropdown();
    }

    private String getLastStoredItem() {
        String result = "";

        String storedData = LocalStorage.readFromFile(mContext, Constants.FRANCHISEE_RESPONSES_CSV);
        String[] rows = storedData.split(";");
        int numOfRows = rows.length;

        if (!storedData.equals("") && numOfRows > 0) {
            result = rows[numOfRows - 1].split(",")[1].trim();
        }
        return result;
    }

    private void updatePrompt() {
        String lastSelectedItem = getLastStoredItem();
        if (!lastSelectedItem.equals("")) {
            lastSelectedItem = String.format("<b>Last time you selected %s customers</b>.", lastSelectedItem);
        }

        String currentPrompt = getResources().getString(R.string.franchisee_prompt);
        String updatePrompt = String.format("%s %s", currentPrompt, lastSelectedItem);
        TextView tvQuestion = findViewById(R.id.tv_question);
        tvQuestion.setText(Html.fromHtml(updatePrompt));
    }

    private void populateDropdown() {
        Spinner spinner = findViewById(R.id.spinner_response);

        String[] dropDownItems = {"2 customers", "4 customers", "6 customers"};
        String lastSelectedItem = getLastStoredItem();
        if (!lastSelectedItem.equals("")) {
            int beginValue = Integer.parseInt(lastSelectedItem) - 2;
            beginValue = beginValue < 0 ? 0 : beginValue;
            dropDownItems = new String[]{
                    String.format("%s customers", beginValue),
                    String.format("%s customers", beginValue + 2),
                    String.format("%s customers", beginValue + 4)
            };

            // make sure user always has the option of selecting zero customers
            if (beginValue > 0) {
                dropDownItems = new String[]{
                        "0 customers",
                        String.format("%s customers", beginValue),
                        String.format("%s customers", beginValue + 2),
                        String.format("%s customers", beginValue + 4)
                };
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, dropDownItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemString = (String) parent.getItemAtPosition(position);
                selectedItem = Integer.parseInt(selectedItemString.split(" ")[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void activateSaveButton() {
        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String row = String.format(Locale.getDefault(), "%d, %s;\n", System.currentTimeMillis(), selectedItem);
                LocalStorage.appendToFile(mContext, Constants.FRANCHISEE_RESPONSES_CSV, row);
                Toast.makeText(mContext, "Response saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
