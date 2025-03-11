package com.example.gbscollectionform;

public class Record {

    //COLLECTION DATA
    double  _totalPrice, _quantity;
    int _id, _collectorId, _junkshopId, _estId, _classification, _color;
    String _imagePath, _street, _barangay, _municipality,
            _province, _region, _vendor,_collector, _date;

    //JUNKSHOP DATA
    int j_id;
    String j_name, j_image, j_vendor, j_street, j_brgy, j_lgu, j_province, j_region, j_date;

    //EST DATA
    int e_id, e_type;
    String e_name, e_image, e_vendor,  e_street, e_brgy, e_lgu, e_province,
            e_region, e_date;

}
