package com.example.gbscollectionform;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PartnersForm extends Fragment {

    private Context con;

    Button submit_button;

    private final List<String> regions = new ArrayList<>();
    private final Map<String, List<String>> provinces = new HashMap<>();
    private final Map<String, List<String>> municipalities = new HashMap<>();
    private final Map<String, List<String>> barangays = new HashMap<>();

    String s_region, s_province, s_municipality, s_barangay,
            s_name, s_street, s_qty, s_agreedprice, s_vendor, s_collector, s_color, s_type;

    EditText street,qty,agreed_price,vendor, collector, name;

    Spinner spinner, est_type;
    private DBHandler dbHandler;

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_partners_form, container, false);
        con = getContext();
        Spinner spinnerRegion = inf.findViewById(R.id.spinnerRegion);
        Spinner spinnerProvince = inf.findViewById(R.id.spinnerProvince);
        Spinner spinnerMunicipality = inf.findViewById(R.id.spinnerMunicipality);
        Spinner spinnerBarangay = inf.findViewById(R.id.spinnerBarangay);


        street = inf.findViewById(R.id.street);
        qty = inf.findViewById(R.id.qty);
        agreed_price = inf.findViewById(R.id.agreed_price);
        vendor = inf.findViewById(R.id.vendor);
        collector = inf.findViewById(R.id.collector);

        submit_button = inf.findViewById(R.id.submit_button);

        dbHandler = new DBHandler(con);

       spinner = inf.findViewById(R.id.spinnerColor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(con,
                R.array.cullets_color, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        est_type = inf.findViewById(R.id.spinnerEstType);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(con,
                R.array.establishment_type, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        est_type.setAdapter(adapter1);

        // Initialize the AutoCompleteTextView
        autoCompleteTextView = inf.findViewById(R.id.estAutoComplete);

        // Set up the adapter (For Auto Suggest)
        List<String> junkshopNames = getestNamesFromDB();
        ArrayAdapter<String> adapter12 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, junkshopNames);
        autoCompleteTextView.setAdapter(adapter12);

        loadData();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(con)
                        .setTitle("Confirm Submission")
                        .setMessage("Are you sure you want to submit this collection and establishment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Retrieve and process input values
                                s_name = autoCompleteTextView.getText().toString();
                                s_region = spinnerRegion.getSelectedItem().toString();
                                s_province = spinnerProvince.getSelectedItem().toString();
                                s_municipality = spinnerMunicipality.getSelectedItem().toString();
                                s_barangay = spinnerBarangay.getSelectedItem().toString();
                                s_street = street.getText().toString();
                                s_qty = qty.getText().toString();
                                s_agreedprice = agreed_price.getText().toString();
                                s_vendor = vendor.getText().toString();
                                s_collector = collector.getText().toString();
                                s_color = spinner.getSelectedItem().toString();
                                s_type = est_type.getSelectedItem().toString();

                                Double int_quant = Double.parseDouble(s_qty);
                                int color = 0;
                                int estab = 0;

                                if (s_color.equals("Flint(Clear)")) {
                                    color += 1;
                                } else if (s_color.equals("Amber(Brown)")) {
                                    color += 2;
                                } else {
                                    color += 3;
                                }

                                switch (s_type) {
                                    case "Sari-sari Store":
                                        estab += 1;
                                        break;
                                    case "Hotel":
                                        estab += 2;
                                        break;
                                    case "Restaurant":
                                        estab += 3;
                                        break;
                                    case "Bar":
                                        estab += 4;
                                        break;
                                    default:
                                        estab += 5;
                                        break;
                                }

                                double d_agreedPrice = Double.parseDouble(s_agreedprice);
                                double total_price = d_agreedPrice * int_quant;

                                dbHandler.addNewEstAndCollection(s_name, s_vendor,
                                        1, 0, 0, s_street, s_barangay, s_municipality, estab, s_province, s_region, int_quant, total_price, color, s_collector, con);


                                /*dbHandler.addCollection(1, 1, 0, s_street, s_barangay, s_municipality,
                                        0, s_province, s_region, int_quant, total_price, color, s_vendor, s_collector);

                                dbHandler.addNewEstablishment(s_name, s_vendor, estab, s_street, s_barangay, s_municipality, s_province, s_region);
                                */
                                // Optional: Show a success message
                                Toast.makeText(con, "Submission successful!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Dismiss the dialog if user selects "No"
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        // Initialize spinners with default values
        initializeSpinners(spinnerRegion, spinnerProvince, spinnerMunicipality, spinnerBarangay);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = regions.get(position);
                List<String> provinceList = new ArrayList<>();
                provinceList.add("Select Province");
                if (provinces.containsKey(selectedRegion)) {
                    provinceList.addAll(Objects.requireNonNull(provinces.get(selectedRegion)));
                }
                ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, provinceList);
                spinnerProvince.setAdapter(provinceAdapter);

                // Reset lower dropdowns
                resetLowerDropdowns(spinnerMunicipality, spinnerBarangay);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = spinnerProvince.getSelectedItem().toString();
                if (!selectedProvince.equals("Select Province")) {
                    List<String> municipalityList = new ArrayList<>();
                    municipalityList.add("Select Municipality");
                    if (municipalities.containsKey(selectedProvince)) {
                        municipalityList.addAll(Objects.requireNonNull(municipalities.get(selectedProvince)));
                    }
                    ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, municipalityList);
                    spinnerMunicipality.setAdapter(municipalityAdapter);

                    // Reset lower dropdown
                    resetLowerDropdown(spinnerBarangay);
                } else {
                    resetLowerDropdowns(spinnerMunicipality, spinnerBarangay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerMunicipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMunicipality = spinnerMunicipality.getSelectedItem().toString();
                if (!selectedMunicipality.equals("Select Municipality")) {
                    List<String> barangayList = new ArrayList<>();
                    barangayList.add("Select Barangay");
                    if (barangays.containsKey(selectedMunicipality)) {
                        barangayList.addAll(Objects.requireNonNull(barangays.get(selectedMunicipality)));
                    }
                    ArrayAdapter<String> barangayAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, barangayList);
                    spinnerBarangay.setAdapter(barangayAdapter);
                } else {
                    resetLowerDropdown(spinnerBarangay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return inf;
    }

    private void initializeSpinners(Spinner spinnerRegion, Spinner spinnerProvince, Spinner spinnerMunicipality, Spinner spinnerBarangay) {
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, regions);
        spinnerRegion.setAdapter(regionAdapter);

        List<String> defaultProvinceList = new ArrayList<>();
        defaultProvinceList.add("Select Province");
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultProvinceList);
        spinnerProvince.setAdapter(provinceAdapter);

        List<String> defaultMunicipalityList = new ArrayList<>();
        defaultMunicipalityList.add("Select Municipality");
        ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultMunicipalityList);
        spinnerMunicipality.setAdapter(municipalityAdapter);

        List<String> defaultBarangayList = new ArrayList<>();
        defaultBarangayList.add("Select Barangay");
        ArrayAdapter<String> barangayAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultBarangayList);
        spinnerBarangay.setAdapter(barangayAdapter);
    }

    private void resetLowerDropdowns(Spinner spinnerMunicipality, Spinner spinnerBarangay) {
        List<String> defaultMunicipalityList = new ArrayList<>();
        defaultMunicipalityList.add("Select Municipality");
        ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultMunicipalityList);
        spinnerMunicipality.setAdapter(municipalityAdapter);

        List<String> defaultBarangayList = new ArrayList<>();
        defaultBarangayList.add("Select Barangay");
        ArrayAdapter<String> barangayAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultBarangayList);
        spinnerBarangay.setAdapter(barangayAdapter);
    }

    private void resetLowerDropdown(Spinner spinnerBarangay) {
        List<String> defaultBarangayList = new ArrayList<>();
        defaultBarangayList.add("Select Barangay");
        ArrayAdapter<String> barangayAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, defaultBarangayList);
        spinnerBarangay.setAdapter(barangayAdapter);
    }

    // Method to fetch junkshop names from SQLite database
    private List<String> getestNamesFromDB() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = requireContext().openOrCreateDatabase("bbpd", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT name FROM establishment", null);

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0)); // Get the "name" column
            } while (cursor.moveToNext());
        }
        cursor.close();
        return names;
    }

    private void loadData() {
        try {
            InputStream is = getResources().openRawResource(R.raw.data);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject data = new JSONObject(json);

            JSONArray regionsArray = data.getJSONArray("regions");
            for (int i = 0; i < regionsArray.length(); i++) {
                JSONObject regionObject = regionsArray.getJSONObject(i);
                String regionName = regionObject.getString("name");
                regions.add(regionName);

                JSONArray provincesArray = regionObject.getJSONArray("provinces");
                List<String> provinceList = new ArrayList<>();
                for (int j = 0; j < provincesArray.length(); j++) {
                    JSONObject provinceObject = provincesArray.getJSONObject(j);
                    String provinceName = provinceObject.getString("name");
                    provinceList.add(provinceName);

                    JSONArray municipalitiesArray = provinceObject.getJSONArray("municipalities");
                    List<String> municipalityList = new ArrayList<>();
                    for (int k = 0; k < municipalitiesArray.length(); k++) {
                        JSONObject municipalityObject = municipalitiesArray.getJSONObject(k);
                        String municipalityName = municipalityObject.getString("name");
                        municipalityList.add(municipalityName);

                        JSONArray barangaysArray = municipalityObject.getJSONArray("barangays");
                        List<String> barangayList = new ArrayList<>();
                        for (int l = 0; l < barangaysArray.length(); l++) {
                            String barangayName = barangaysArray.getString(l);
                            barangayList.add(barangayName);
                        }
                        barangays.put(municipalityName, barangayList);
                    }
                    municipalities.put(provinceName, municipalityList);
                }
                provinces.put(regionName, provinceList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
