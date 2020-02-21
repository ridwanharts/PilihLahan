package com.labs.jangkriek.carilahan.Utils;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public String formatDecimal(double bilangan){

        return String.format("%.2f", bilangan);
    }

    public String format5(double bilangan){

        return String.format("%.5f", bilangan);
    }

    public List<LatLng> listPointJalanRayaWates (){

        //jalan raya wates
        List<LatLng> point = new ArrayList<LatLng>();
        point.add(new LatLng(-7.890081319,110.1061647));
        point.add(new LatLng(-7.889902593,110.1042229));
        point.add(new LatLng(-7.889780943,110.1033495));
        point.add(new LatLng(-7.890042052,110.10518));
        point.add(new LatLng(-7.889610767,110.1023244));
        point.add(new LatLng(-7.889452243,110.1013512));
        point.add(new LatLng(-7.889143853,110.0994606));
        point.add(new LatLng(-7.888941703,110.0985044));
        point.add(new LatLng(-7.888817551,110.0975858));
        point.add(new LatLng(-7.889311504,110.1004466));
        point.add(new LatLng(-7.888661565,110.0966976));
        point.add(new LatLng(-7.888552807,110.0956555));
        point.add(new LatLng(-7.88807693,110.0931046));
        point.add(new LatLng(-7.88840309,110.0948533));
        point.add(new LatLng(-7.888242115,110.0939833));
        point.add(new LatLng(-7.887982836,110.0922254));
        point.add(new LatLng(-7.887804933,110.0913106));
        point.add(new LatLng(-7.88761524,110.0903334));
        point.add(new LatLng(-7.887367787,110.0884227));
        point.add(new LatLng(-7.887509696,110.0893805));
        point.add(new LatLng(-7.887295058,110.0875665));
        point.add(new LatLng(-7.887266241,110.0866036));
        point.add(new LatLng(-7.887223635,110.0857809));
        point.add(new LatLng(-7.887157867,110.0849007));
        point.add(new LatLng(-7.887134068,110.0840616));
        point.add(new LatLng(-7.887075792,110.0831839));
        point.add(new LatLng(-7.887018087,110.0823538));
        point.add(new LatLng(-7.886990864,110.0814927));
        point.add(new LatLng(-7.886921905,110.0797919));
        point.add(new LatLng(-7.886941294,110.0805712));
        point.add(new LatLng(-7.886942294,110.079031));
        point.add(new LatLng(-7.886917595,110.078209));
        point.add(new LatLng(-7.886812267,110.0773267));
        point.add(new LatLng(-7.886737964,110.0753168));
        point.add(new LatLng(-7.886837881,110.0764393));
        point.add(new LatLng(-7.886684621,110.0746538));
        point.add(new LatLng(-7.886645891,110.0737671));
        point.add(new LatLng(-7.886602738,110.0729408));
        point.add(new LatLng(-7.886643282,110.072035));
        point.add(new LatLng(-7.886565032,110.071006));
        point.add(new LatLng(-7.886465393,110.0700532));
        point.add(new LatLng(-7.886446667,110.0690866));
        point.add(new LatLng(-7.886393013,110.0681471));
        point.add(new LatLng(-7.886303053,110.0671344));
        point.add(new LatLng(-7.886277602,110.0661655));
        point.add(new LatLng(-7.886218839,110.065216));
        point.add(new LatLng(-7.88619121,110.0643536));
        point.add(new LatLng(-7.886264956,110.0633867));
        point.add(new LatLng(-7.886318544,110.062419));
        point.add(new LatLng(-7.886384741,110.061518));
        point.add(new LatLng(-7.886419538,110.0605729));
        point.add(new LatLng(-7.886489711,110.0597426));
        point.add(new LatLng(-7.886486756,110.0590126));
        point.add(new LatLng(-7.886605654,110.0574357));
        point.add(new LatLng(-7.886596823,110.0583206));
        point.add(new LatLng(-7.88666751,110.0566995));
        point.add(new LatLng(-7.886698287,110.0559083));
        point.add(new LatLng(-7.886704256,110.0550051));
        point.add(new LatLng(-7.8863411,110.0529113));
        point.add(new LatLng(-7.886676739,110.0539713));
        point.add(new LatLng(-7.885986465,110.0519585));
        point.add(new LatLng(-7.884767632,110.0499514));
        point.add(new LatLng(-7.883300299,110.0474047));
        point.add(new LatLng(-7.883967883,110.0486993));
        point.add(new LatLng(-7.885512177,110.0509457));
        point.add(new LatLng(-7.88249564,110.046121));
        point.add(new LatLng(-7.881634185,110.044863));
        point.add(new LatLng(-7.880921879,110.0437155));
        point.add(new LatLng(-7.890081319,110.1061647));
        point.add(new LatLng(-7.880490687,110.0431626));

        //Galur
        point.add(new LatLng(-7.9516730221612155,110.20634439538702));
        point.add(new LatLng(-7.951727779670881,110.20578920053498));
        point.add(new LatLng(-7.951798961323211,110.20507834754041));
        //Girimulyo
        point.add(new LatLng(-7.75037104265132,110.19589804779343));
        point.add(new LatLng(-7.750026494254778,110.1944036347781));
        point.add(new LatLng(-7.750133412245959,110.19527640643123));
        //kalibawang
        point.add(new LatLng(-7.719770588195483,110.22309172786011));
        point.add(new LatLng(-7.719879369835059,110.22303683957074));
        point.add(new LatLng(-7.719685602808841,110.22314318567913));
        //kokap
        point.add(new LatLng(-7.875451369948707,110.08285552562751));
        point.add(new LatLng(-7.875201379938248,110.08164379857527));
        point.add(new LatLng(-7.875342039599875,110.08216761799656));
        //lendah
        point.add(new LatLng(-7.902744386155625,110.20253042197635));
        point.add(new LatLng(-7.903591415481194,110.20243260081612));
        point.add(new LatLng(-7.90309445016029,110.20250833341845));
        //nanggulan
        point.add(new LatLng(-7.787831423707665,110.19510152129442));
        point.add(new LatLng(-7.788169495494045,110.19507845330276));
        point.add(new LatLng(-7.788867812889508,110.19501234665472));
        //pengasih
        point.add(new LatLng(-7.794711626889326,110.15431875329786));
        point.add(new LatLng(-7.794414106070745,110.15415516215147));
        point.add(new LatLng(-7.794931261033306,110.15454043619265));
        //samigaluh
        point.add(new LatLng(-7.713766337246818,110.22810220221606));
        point.add(new LatLng(-7.715536708914557,110.22604232176184));
        point.add(new LatLng(-7.712511067010785,110.22941634635282));
        //sentolo
        point.add(new LatLng(-7.8644368754396385,110.21094563055529));
        point.add(new LatLng(-7.8647338321852445,110.21085254236122));
        point.add(new LatLng(-7.865077676519941,110.2107704984196));
        //temon
        point.add(new LatLng(-7.890583420114031,110.04979616860328));
        point.add(new LatLng(-7.890265351086505,110.04926433110705));
        point.add(new LatLng(-7.8896292123392895,110.0487023903222));
        //wates
        point.add(new LatLng(-7.866258525966324,110.16854040478137));
        point.add(new LatLng(-7.866411692432938,110.16812549731237));
        point.add(new LatLng(-7.866629891628313,110.16759536677665));
        //panjatan
        point.add(new LatLng(-7.908448529692045,110.1523949622545));
        point.add(new LatLng(-7.908696340518836,110.15239496265282));
        point.add(new LatLng(-7.909049385952407,110.15246693548926));
        
        return point;
    }
}
