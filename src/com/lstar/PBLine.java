package com.lstar;

public class PBLine {
    private int rowNumber;
    private int compileRowNumber;
    private String base;
    private String normalizedBase;
    private String comment;
    private boolean runnableLine;
    private String callName;
    private InstructionBits bits;

    //Constructor
    PBLine(int rowNumber, String base){
        this.rowNumber = rowNumber;
        this.base = base;

        normalize();
        defConstants();
        defNameregs();
        defCalls();
    }

    public int getIntBits(){
        if (bits!=null)
        return bits.getInstructionInt();
        else return 0;
    }
    //Generate instuctionBits
    public void generateInstuctionBits(){
        if(runnableLine){
            try {
                //System.out.println(normalizedBase);
                bits = new InstructionBits(normalizedBase);
            } catch (Exception e) {
                if(e instanceof Erro){
                    ((Erro) e).Err(rowNumber, normalizedBase);
                    //e.printStackTrace();
                }else {
                    System.out.println("\u001B[32m" + e.toString() + " at line: " + rowNumber + "\u001B[0m");
                    System.out.println(e.toString() + " at line: " + rowNumber);
                    e.printStackTrace();
                }
            }
        }
    }
    //Create constants
    private void defConstants(){
        if(normalizedBase == null) return;

        String tokens[] = normalizedBase.split(" ");
        if(tokens[0].trim().toUpperCase().equals("CONSTANT")){
            if(tokens.length < 3){
                Erro.noEnoughParameter(rowNumber,"Constant");
                return;
            }
            String op1 = tokens[1].trim();
            String op2 = tokens[2].trim();

            if(PicoBlazeCompilator.constant.containsKey(op1)){
                Erro.alreadyDefined(rowNumber, "Constant");
                return;
            }else {
                PicoBlazeCompilator.constant.put(op1,op2);
                runnableLine = false;
            }
        }
    }

    //Create nameregs
    private void defNameregs(){
        if(normalizedBase == null) return;

        String tokens[] = normalizedBase.split(" ");
        if(tokens[0].trim().toUpperCase().equals("NAMEREG")){
            if(tokens.length < 3){
                Erro.noEnoughParameter(rowNumber,"Namereg");
                return;
            }
            String op1 = tokens[1].trim();
            String op2 = tokens[2].trim();

            if(PicoBlazeCompilator.namereg.containsKey(op2)){
                Erro.alreadyDefined(rowNumber, "Namereg");
                return;
            }else {
                PicoBlazeCompilator.namereg.put(op2,op1.toUpperCase());
                runnableLine = false;
            }
        }
    }

    //Create calls
    private void defCalls(){
        if(normalizedBase == null) return;
        if(!normalizedBase.contains(":")) return;

        String tokens[] = normalizedBase.split(":");
        if(tokens.length == 1){
            String callName = tokens[0].trim();
            if(PicoBlazeCompilator.call.containsKey(callName)){
                Erro.alreadyDefined(rowNumber, "Call Name");
                return;
            }
            PicoBlazeCompilator.call.put(callName, 0);
            this.callName = callName;
            runnableLine = false;
        }
        if(tokens.length > 1){
            String callName = tokens[0].trim();
            if(PicoBlazeCompilator.call.containsKey(callName)){
                Erro.alreadyDefined(rowNumber, "Call Name");
                return;
            }
            PicoBlazeCompilator.call.put(callName, 0);
            this.callName = callName;

            normalizedBase = tokens[1].trim();

        }
    }

    /*
     * Normalize
     * Create normalizedBase only
     * Create comment
     */
    private void normalize(){
        if(base.contains(";")){
            comment = base.split(";")[1];
            normalizedBase = base.split(";")[0];
        }else {
            normalizedBase = base;
        }

        //Remove all blank characters
        normalizedBase = normalizedBase.replaceAll("\\(", " ");
        normalizedBase = normalizedBase.replaceAll("\\)", " ");
        normalizedBase = normalizedBase.replaceAll("\t", " ");
        normalizedBase = normalizedBase.replaceAll(",", " ");
        normalizedBase = normalizedBase.replaceAll(" +", " ");
        normalizedBase = normalizedBase.trim();
        if(normalizedBase.length() > 1){
            runnableLine = true;
        }else{
            normalizedBase = null;
        }
    }

    public void setCompileRowNumber(int n){
        this.compileRowNumber = n;
    }

    public boolean isRunnableLine() {
        return runnableLine;
    }

    public String getCallName() {
        return callName;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void printNormalized(){
        if(normalizedBase != null) {
            System.out.println(rowNumber + "\t" +normalizedBase);
        }
    }

    public void printRunnable(){
        if(runnableLine){
            System.out.println(rowNumber + "\t" +normalizedBase );
        }
    }

    public void printCompillable(){
        if(runnableLine){
            System.out.println(compileRowNumber + "\t" +normalizedBase );
        }
    }

}
