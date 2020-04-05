import sun.security.mscapi.CKeyPairGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.interfaces.RSAPrivateKey;

public class HexOps {
    private static String ChangeDecimal(String value) {
        StringBuilder out = new StringBuilder();
        char[] bytes = value.toCharArray();
        for (int i = 0; i < bytes.length; i += 2) {
            out.append(String.valueOf(bytes[bytes.length - i - 2]));
            out.append(String.valueOf(bytes[bytes.length - 1 - i]));
        }

        return out.toString();
    }
    private static int Hex2Int(String in){return Integer.parseInt(in, 16);};
    private static String Double2Hex(double in) {
        return ChangeDecimal(Long.toHexString(Double.doubleToLongBits(in)));
    }
    private static double Hex2Double(String in) {return Double.longBitsToDouble(Long.parseLong(ChangeDecimal(in), 16));}

    private static String Byte2String(byte in){
        String output = Integer.toHexString((int) in & 0xff);
        if ((in& 0xff) < 16) {
            output = "0" + output;
        }
        return  output;
    }
    public static String[] Regs(Interface inter, boolean RegB) {
        File ftt = new File(inter.dir, inter.name);
        Path path = ftt.toPath();
        try {
            if(inter.bytes == null) inter.bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output;
        StringBuilder o = new StringBuilder();
        String[] out = new String[inter.regsize];
        int c = 0;
        int lineskip =inter.lineshift;
        int s = 0;
        if(RegB) s = inter.line;
        inter.line = 0;
        inter.fline = 0;
        if(!RegB)for (int i = inter.regstart; i < inter.regstart+inter.regsize; i++) {
            output = Byte2String(inter.bytes[i]);
            c++;
            if(lineskip == inter.lineskip || i <= inter.varsize) o.append(output);
                if (c % inter.varsize == 0) {
                    lineskip++;
                    if(lineskip-1 == inter.lineskip || i <= inter.varsize){
                c = 0;
                    out[inter.line] = o.toString();
                o = new StringBuilder();
                lineskip = 0;
                inter.line++;
                    }

            }
        }
        if(RegB)for (int i = inter.regstart; i < inter.regstart+(s*(inter.lineskip+1)*inter.varsize); i++) {
            output = Byte2String(inter.bytes[i]);
            c++;
            if(lineskip == inter.lineskip || i <= inter.varsize) o.append(output);
            if (c % inter.varsize == 0) {
                lineskip++;
                if(lineskip-1 == inter.lineskip || i <= inter.varsize){
                    c = 0;
                    out[inter.line] = o.toString();
                    o = new StringBuilder();
                    lineskip = 0;
                    inter.line++;
                }

            }
        }
        for(int i = 0; i<inter.line; i++){ out[i] = "0x"+ChangeDecimal(out[i]);
        }

        if(RegB) inter.line = s;
        return out;
    }
    private static int ReadInt(Interface inter, int ind){
        return Hex2Int(ChangeDecimal(Byte2String(inter.bytes[inter.regAstart + ind]) + Byte2String(inter.bytes[inter.regAstart + ind + 1])));
    }
    public static void GetCaps(Interface inter) {
        inter.HeightRes = ReadInt(inter,inter.relativeHeightInd);
        inter.WidthRes = Hex2Int(ChangeDecimal(Byte2String(inter.bytes[inter.regAstart + inter.relativeWidthInd]) + Byte2String(inter.bytes[inter.regAstart + inter.relativeWidthInd + 1])));
        int ind = inter.CapStart + inter.relativeFrameL;
        inter.FrameL = Hex2Int(ChangeDecimal(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1])));
        ind = inter.CapStart + inter.relativeLineL;
        inter.LineL = Hex2Int(ChangeDecimal(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1])));
        ind = inter.CapStart + inter.relativepInd;
        inter.pixelclockCap = Long.parseLong(ChangeDecimal(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1]) + Byte2String(inter.bytes[ind + 2]) + Byte2String(inter.bytes[ind + 3])), 16);
        inter.last = ind;
        ind = inter.CapStart + inter.relativeBInd;
        inter.bincap1 = (short) Hex2Int(ChangeDecimal(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1])));
        ind += 8;
        inter.bincap2 = (short) Hex2Int(ChangeDecimal(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1])));
        ind = inter.CapStart + inter.relativefInd;
        inter.FpsCap = Hex2Double(Byte2String(inter.bytes[ind]) + Byte2String(inter.bytes[ind + 1]) + Byte2String(inter.bytes[ind + 2]) + Byte2String(inter.bytes[ind + 3])
        +Byte2String(inter.bytes[ind+4]) + Byte2String(inter.bytes[ind + 5]) + Byte2String(inter.bytes[ind + 6]) + Byte2String(inter.bytes[ind + 7])
        );
    }
    private static void SaveInt(Interface inter, int integ, int index){
        String test = Integer.toString(integ, 16);
        if(test.length() %2 !=0) test = "0"+test;
        byte[] output = String2Byte(test);
        //if(output.length > 1)
        inter.bytes[index] = output[1];
        inter.bytes[index+1] = output[0];
    }
    private static void SaveShort(Interface inter, int integ, int index){
        inter.bytes[index] = (byte)integ;
    }
    private static void SaveLong(Interface inter, long integ, int index){
        short k = 0;
        inter.bytes[index+k] = String2Byte(Long.toHexString(integ))[3-k];
        k++;
        inter.bytes[index+k] = String2Byte(Long.toHexString(integ))[3-k];
        k++;
        inter.bytes[index+k] = String2Byte(Long.toHexString(integ))[3-k];
        k++;
        inter.bytes[index+k] = String2Byte(Long.toHexString(integ))[3-k];
    }
    private static void SaveDouble(Interface inter, double integ, int index){
        for(int l =0; l<8;l++) {
            inter.bytes[index + l] = String2Byte(Double2Hex(integ))[l];
        }
    }
    private static void SaveCaps(Interface inter){
        int ind = inter.CapStart+inter.relativeFrameL;
        SaveInt(inter, inter.FrameL, ind);
        ind = inter.CapStart + inter.relativeLineL;
        SaveInt(inter, inter.LineL, ind);
        ind = inter.CapStart + inter.relativepInd;
        SaveLong(inter, inter.pixelclockCap, ind);
        ind = inter.regAstart + inter.relativeHeightInd;
        SaveInt(inter, inter.HeightRes, ind);
        ind = inter.regAstart + inter.relativeWidthInd;
        SaveInt(inter, inter.WidthRes, ind);
        ind = inter.CapStart + inter.relativeBInd;
        SaveShort(inter,inter.bincap1, ind);
        SaveShort(inter,inter.bincap2, ind+8);
        ind = inter.CapStart + inter.relativefInd;
        SaveDouble(inter, inter.FpsCap, ind);
    }
  //  private static byte[] String2Byte(String in){return DatatypeConverter.parseHexBinary(in);}
        private static byte[] String2Byte(String in){
        in = in.replace("0x", "");
        char[] i = in.toCharArray();
        byte[] output = new byte[i.length];
        for(int k = 0; k<i.length; k+=2) {
            output[k / 2] += -128;
            output[k / 2] = (byte) (Integer.parseInt((Character.toString(i[k]) + Character.toString(i[k + 1])), 16));
        }
        return output;
    }


    private static void BytePath(Interface inter){
        for(int i =0; i<inter.line; i++) {
                inter.bytes[inter.regstart + inter.lineskip + i * (inter.lineskip + 1)] = String2Byte(inter.regvalue[i].replace("0x", ""))[0];
        }
    }
    public static void SafeRegs(Interface inter) {
        System.out.println("SafeRegs: "+inter.regname.length);
        File f = new File(inter.dir, inter.name);
        Path path = f.toPath();
        BytePath(inter);
        SaveCaps(inter);
        try {
            Files.write(path, inter.bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
