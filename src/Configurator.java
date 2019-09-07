import java.io.*;
import java.util.Properties;

public class Configurator {
    public static void CreateProps(String libname){
        File conf = new File(libname.replace(".bin", ".ini"));
        boolean getExample = false;
        try {
            if(!conf.exists()) {
                conf.createNewFile();
                getExample = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(getExample) Example(conf);//props = Example(props);
    }
    private static void Example(File conf){
        FileWriter fw = null;
        try {
            fw = new FileWriter(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Writeln(fw,"#Eszdman Tech. Registers from libs.");
        Writeln(fw,"#DeveloperOpt");
        Writeln(fw, "VarSizeA"+"="+ "2");
        Writeln(fw, "LineSkipA"+"="+ "39");
        Writeln(fw, "LineShiftA"+"="+ "0");
        Writeln(fw, "VarSizeB"+"="+ "1");
        Writeln(fw, "LineSkipB"+"="+ "15");
        Writeln(fw, "LineShiftB"+"="+ "0");
        Writeln(fw, "RelativeWidthInd"+"="+ "-30");
        Writeln(fw, "RelativeHeightInd"+"="+ "-28");
        Writeln(fw, "RelativeLineL"+"="+ "0");
        Writeln(fw, "RelativeFrameL"+"="+ "8");
        Writeln(fw, "RelativePixelClockInd"+"="+ "20");
        Writeln(fw, "RelativeBinInd"+"="+ "28");
        Writeln(fw, "RelativeFPSInd"+"="+ "38");
        Writeln(fw, "RelativeModeInd"+"="+ "40");
        Writeln(fw, "");
        Writeln(fw, "#Settings");

        Writeln(fw, "ResName"+"="+ "res0");
        //Writeln(fw, "ResWidthInd"+"="+ "4DB18");
        //Writeln(fw, "ResHeighInd"+"="+ "4DB20");
        Writeln(fw, "ResCapStart"+"="+ "4D4B0");
        Writeln(fw, "RegStartA"+"="+ "4DB48");
        Writeln(fw, "RegSizeA"+"="+ "1F80");
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void Writeln(FileWriter fw, String in){
        try {
            fw.write(in);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void GetProps(String libname,Interface inter, boolean RegB){
        File conf = new File(libname.replace(".bin", ".ini"));
        try {
            conf.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(conf));
        } catch (IOException e) {
            e.printStackTrace();
        }
        inter.naming = props.getProperty("ResName");
        if(!RegB) {
           inter.varsize =Integer.parseInt( props.getProperty("VarSizeA"));
           inter.lineskip =Integer.parseInt( props.getProperty("LineSkipA"));
           inter.lineshift =Integer.parseInt( props.getProperty("LineShiftA"));
           inter.regstart =Integer.parseInt(props.getProperty("RegStartA"),16) - inter.lineskip*inter.varsize;
           inter.regsize =Integer.parseInt( props.getProperty("RegSizeA"),16);
           //inter.hInd = Integer.parseInt( props.getProperty("ResHeighInd"),16);
           //inter.wInd = Integer.parseInt( props.getProperty("ResWidthInd"),16);
           inter.CapStart = Integer.parseInt( props.getProperty("ResCapStart"),16);
           inter.relativeFrameL = Integer.parseInt( props.getProperty("RelativeFrameL"),16);
           inter.relativeLineL = Integer.parseInt( props.getProperty("RelativeLineL"),16);
           inter.relativepInd = Integer.parseInt( props.getProperty("RelativePixelClockInd"),16);
           inter.relativeBInd = Integer.parseInt( props.getProperty("RelativeBinInd"),16);
           inter.relativefInd = Integer.parseInt(props.getProperty("RelativeFPSInd"), 16);
           inter.relativeWidthInd = Integer.parseInt(props.getProperty("RelativeWidthInd"), 16);
           inter.relativeHeightInd = Integer.parseInt(props.getProperty("RelativeHeightInd"), 16);
        }
        if(RegB){
            inter.varsize =Integer.parseInt( props.getProperty("VarSizeB"));
            inter.lineskip =Integer.parseInt( props.getProperty("LineSkipB"));
            inter.lineshift =Integer.parseInt( props.getProperty("LineShiftB"));
            //inter.regstart =Integer.parseInt(props.getProperty("RegStartB"),16) - inter.lineskip*inter.varsize;
            //inter.regsize =Integer.parseInt( props.getProperty("RegSizeB"),16);
        }

    }
    private static Properties Example(Properties props){
        props.setProperty("ResName", "res0");
        props.setProperty("VarSizeA", Integer.toString(2));
        props.setProperty("LineSkipA",Integer.toString(39));
        props.setProperty("LineShiftA",Integer.toString(0));
        props.setProperty("RegStartA",Integer.toString(0x4DB48, 16).toUpperCase());
        props.setProperty("RegSizeA",Integer.toString(0x1F80, 16).toUpperCase());
        props.setProperty("VarSizeB",Integer.toString(1));
        props.setProperty("LineSkipB",Integer.toString(15));
        props.setProperty("LineShiftB",Integer.toString(0));
        props.setProperty("ResWidthInd",Integer.toString(0x4DB18, 16).toUpperCase());
        props.setProperty("ResHeighInd",Integer.toString(0x4DB20,16).toUpperCase());
        props.setProperty("ResCapStart",Integer.toString(0x4D4B0,16).toUpperCase());
        props.setProperty("RelativeLineL",Integer.toString(0,16).toUpperCase());
        props.setProperty("RelativeFrameL",Integer.toString(8,16).toUpperCase());
        props.setProperty("RelativePixelClockInd",Integer.toString(0x20,16).toUpperCase());
        props.setProperty("RelativeBinInd",Integer.toString(0x28,16).toUpperCase());
        props.setProperty("RelativeFPSInd",Integer.toString(0x38,16).toUpperCase());
        props.setProperty("RelativeModeInd",Integer.toString(0x40,16).toUpperCase());

        //props.setProperty("RegStartB",Integer.toString(0x4FAC8, 16).toUpperCase());
        //props.setProperty("RegSizeB",Integer.toString(0x648, 16).toUpperCase());
        return props;
    }
}
