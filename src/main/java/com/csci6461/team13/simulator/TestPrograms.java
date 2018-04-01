package com.csci6461.team13.simulator;

import com.csci6461.team13.simulator.ui.basic.Program;
import com.csci6461.team13.simulator.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiyuan
 * <p>
 * TestPrograms for test purpose only
 * <p>
 * this file contains two programs that will be used for developing and
 * testing programs, and then you can generate binary format of these programs
 * <p>
 * Note: initial data can only be stored in addresses under 32, any address above
 * can not be directly accessed
 */
public final class TestPrograms {

    // use space to separate numbers
    private static final int PROGRAM_1_SEPARATOR = 32;
    private static final int PROGRAM_1_MAX = 20;

    // sentence separator: period
    private static final int PROGRAM_2_SEN_SEPARATOR = 46;
    // word separator: blank space
    private static final int WORD_SEPARATOR_BLANK = 32;
    // word separator: comma
    private static final int WORD_SEPARATOR_COMMA = 44;
    // max 6 sentences
    private static final int PROGRAM_2_MAX = 6;

    // char preset
    private static final int CHAR_0 = 48;
    private static final int CHAR_EQUAL = 61;

    private static final Program one = new Program();
    private static final Program two = new Program();

    static {
        two.setDescription("A program that reads a set of a paragraph of 6 sentences from a file into memory. It prints the sentences on the console printer. It then asks the user for a word. It searches the paragraph to see if it contains the word. If so, it prints out the word, the sentence number, and the word number in the sentence.");
        two.setInitAddrIndex(Const.PROG_ADDR_POINTER);
        List<String> init = new ArrayList<>();
        List<String> loop = new ArrayList<>();
        List<String> sentenceReader = new ArrayList<>();
        List<String> wordReader = new ArrayList<>();
        List<String> comparator = new ArrayList<>();
        List<String> wordFinder = new ArrayList<>();
        List<String> equal = new ArrayList<>();
        List<String> printer = new ArrayList<>();
        List<String> period = new ArrayList<>();
        List<String> comma = new ArrayList<>();
        List<String> blank = new ArrayList<>();
        List<String> errorPrinter = new ArrayList<>();
        List<String> skipper = new ArrayList<>();

        two.putInitData(30, PROGRAM_2_MAX);
        two.putInitData(6, WORD_SEPARATOR_BLANK);
        two.putInitData(7, PROGRAM_2_SEN_SEPARATOR);
        two.putInitData(8, WORD_SEPARATOR_COMMA);
        // storage start
        two.putInitData(31, 500);
        // 6 blank separator
        // 7 sentence separator
        // 8 comma separator
        // 10 current char, and word char address pointer for comparator
        // 11 storage pointer, current store address
        // 12 sentence count, a variable
        // 13 return address pointer for reader
        // 14 word count
        // 15 word start pointer, a constant
        // 30 max sentence count, a constant
        // 31 storage begin address constant

        two.putModule(Const.PROG_ADDR_POINTER, init);
        two.putModule(18, loop);
        two.putModule(19, sentenceReader);
        two.putModule(20, wordReader);
        two.putModule(21, wordFinder);
        two.putModule(22, comparator);
        two.putModule(23, equal);
        two.putModule(24, printer);
        two.putModule(25, period);
        two.putModule(26, comma);
        two.putModule(27, blank);
        two.putModule(28, errorPrinter);
        two.putModule(29, skipper);

        // set return address of reader to loop start
        init.add("LDR 0,0,0,18");
        init.add("STR 0,0,0,13");
        // init storage index variable
        init.add("LDR 0,0,0,31");
        init.add("STR 0,0,0,11");
        // jump to reader
        init.add("JMA 0,0,1,19");

        // loop
        // load sentence count
        loop.add("LDR 0,0,0,12");
        // check if count == Max
        loop.add("SMR 0,0,0,30");
        // not zero, jump to reader
        loop.add("JNE 0,0,1,19");
        // count reached max
        // store the word address to 15
        loop.add("LDR 0,0,0,11");
        loop.add("STR 0,0,0,15");
        // read one word
        loop.add("JSR 0,0,1,20");
        // reset sentence count to 0
        loop.add("LDA 0,0,0,0");
        loop.add("STR 0,0,0,12");
        // init word count to 0
        loop.add("LDA 0,0,0,0");
        loop.add("STR 0,0,0,14");
        // reset storage index variable to init value
        loop.add("LDR 0,0,0,31");
        loop.add("STR 0,0,0,11");
        // init r2 with sentence word begin address
        loop.add("LDR 2,0,0,31");
        // reset word char address pointer to word begin address
        loop.add("LDR 0,0,0,15");
        loop.add("STR 0,0,0,10");
        // jump to word finder
        loop.add("JMA 0,0,1,22");

        // reader, read a complete sentence
        sentenceReader.add("IN 0,0,0,2");
        sentenceReader.add("OUT 0,0,0,1");
        // store char to both 10 and storage
        sentenceReader.add("STR 0,0,0,10");
        sentenceReader.add("STR 0,0,1,11");
        // increase storage index by 1
        // load storage index
        sentenceReader.add("LDR 0,0,0,11");
        // increase index by 1
        sentenceReader.add("AIR 0,0,0,1");
        // store new storage index
        sentenceReader.add("STR 0,0,0,11");
        // check if this is an sentence end
        // load current char
        sentenceReader.add("LDR 0,0,0,10");
        // subtract separator, a constant
        sentenceReader.add("SMR 0,0,0,7");
        // if it's not a sentence end
        // read next char
        sentenceReader.add("JNE 0,0,1,19");
        // else
        // increase write count by 1
        // then return to stored address
        // load sentence count
        sentenceReader.add("LDR 0,0,0,12");
        // increase count by 1
        sentenceReader.add("AIR 0,0,0,1");
        // store new sentence count
        sentenceReader.add("STR 0,0,0,12");
        // return to stored address
        sentenceReader.add("JMA 0,0,1,13");

        // read a char
        wordReader.add("IN 0,0,0,0");
        // print the char
        wordReader.add("OUT 0,0,0,1");
        // store char to both 10 and storage index
        wordReader.add("STR 0,0,0,10");
        wordReader.add("STR 0,0,1,11");
        // increase storage index
        // load storage index
        wordReader.add("LDR 0,0,0,11");
        // increase index by 1
        wordReader.add("AIR 0,0,0,1");
        // store new storage index
        wordReader.add("STR 0,0,0,11");
        // load current char
        wordReader.add("LDR 0,0,0,10");
        // subtract separator, a constant
        wordReader.add("SMR 0,0,0,6");
        // if it's not a word end
        // read next char
        wordReader.add("JNE 0,0,1,20");
        // return
        wordReader.add("RFS 0,0,0,0");

        // load next char in original word
        comparator.add("LDR 0,0,1,10");
        // test equality
        comparator.add("SMR 0,0,1,11");
        // jump to equal if equal
        comparator.add("JZ 0,0,1,23");
        // load next char in original word
        comparator.add("LDR 0,0,1,11");
        // if c(r1) = '.', then this is both a word end and a sentence end
        comparator.add("SMR 0,0,0,7");
        // jump to equal if equal
        comparator.add("JZ 0,0,1,23");
        // load next char in sentence word
        comparator.add("LDR 0,0,1,11");
        // if c(r1) = ',', then this is both a word end and a sentence end
        comparator.add("SMR 0,0,0,8");
        // jump to equal if equal
        comparator.add("JZ 0,0,1,23");
        // not equal, to next word
        // reset word index to begin
        comparator.add("LDR 0,0,0,15");
        comparator.add("STR 0,0,0,10");
        // to skipper
        comparator.add("JMA 0,0,1,29");

        // increase storage index by 1
        wordFinder.add("LDR 0,0,0,11");
        wordFinder.add("AIR 0,0,0,1");
        wordFinder.add("STR 0,0,0,11");
        // check blank
        // load next char in storage
        wordFinder.add("LDR 0,0,1,11");
        // check if it's a blank space
        wordFinder.add("SMR 0,0,0,6");
        // jump if equal
        wordFinder.add("JZ 0,0,1,27");
        // check period
        // load next char in storage
        wordFinder.add("LDR 0,0,1,11");
        // check if it's a period
        wordFinder.add("SMR 0,0,0,7");
        // jump if equal
        wordFinder.add("JZ 0,0,1,25");
        // check comma
        // load next char in storage
        wordFinder.add("LDR 0,0,1,11");
        // check if it's a comma
        wordFinder.add("SMR 0,0,0,8");
        // try next char
        wordFinder.add("JZ 0,0,1,21");
        // found one, return
        wordFinder.add("JMA 0,0,1,22");

        // check the original char is a blank
        equal.add("LDR 0,0,1,10");
        equal.add("SMR 0,0,0,6");
        // found one, jump to printer
        equal.add("JZ 0,0,1,24");
        // else, return to comparator
        // increase storage index by 1
        equal.add("LDR 0,0,0,11");
        equal.add("AIR 0,0,0,1");
        equal.add("STR 0,0,0,11");
        // increase word char index by 1
        equal.add("LDR 0,0,0,10");
        equal.add("AIR 0,0,0,1");
        equal.add("STR 0,0,0,10");
        // return
        equal.add("JMA 0,0,1,22");

        // increase sentence count by 1
        period.add("LDR 0,0,0,12");
        period.add("AIR 0,0,0,1");
        period.add("STR 0,0,0,12");
        // if sentence count reached max
        // to finish, found none
        period.add("SMR 0,0,0,30");
        period.add("JZ 0,0,1,28");
        // reset word count to 0
        period.add("LDA 0,0,0,0");
        period.add("STR 0,0,0,14");
        // increase storage index by 1
        period.add("LDR 0,0,0,11");
        period.add("AIR 0,0,0,1");
        period.add("STR 0,0,0,11");
        // skip if next char is blank
        period.add("LDR 0,0,1,11");
        period.add("SMR 0,0,0,6");
        period.add("JZ 0,0,1,21");
        // else reset to previous next char
        period.add("LDR 0,0,0,11");
        period.add("SIR 0,0,0,1");
        period.add("STR 0,0,0,11");
        period.add("JMA 0,0,1,21");

        // increase word count by 1
        blank.add("LDR 0,0,0,14");
        blank.add("AIR 0,0,0,1");
        blank.add("STR 0,0,0,14");
        blank.add("JMA 0,0,1,21");

        // print sentence count
        printer.add("LDR 0,0,0,12");
        printer.add("AIR 0,0,0,1");
        printer.add("STR 0,0,0,12");
        printer.add("LDR 0,0,0,12");
        printer.add("OUT 0,0,1,1");
        printer.add("LDR 0,0,0,8");
        printer.add("OUT 0,0,0,1");
        // print word count
        printer.add("LDR 0,0,0,14");
        printer.add("AIR 0,0,0,1");
        printer.add("STR 0,0,0,14");
        printer.add("LDR 0,0,0,14");
        printer.add("OUT 0,0,1,1");
        printer.add("HLT 0,0,0,0");

        // '0'
        errorPrinter.add("LDA 0,0,0,0");
        errorPrinter.add("OUT 0,0,1,1");
        // ','
        errorPrinter.add("LDR 0,0,0,8");
        errorPrinter.add("OUT 0,0,0,1");
        // '0'
        errorPrinter.add("LDA 0,0,0,0");
        errorPrinter.add("OUT 0,0,1,1");
        errorPrinter.add("HLT 0,0,0,0");

        // increase storage index by 1
        skipper.add("LDR 0,0,0,11");
        skipper.add("AIR 0,0,0,1");
        skipper.add("STR 0,0,0,11");
        // check blank
        // load next char in storage
        skipper.add("LDR 0,0,1,11");
        // check if it's a blank space
        skipper.add("SMR 0,0,0,6");
        // jump if equal
        skipper.add("JZ 0,0,1,27");
        // check period
        // load next char in storage
        skipper.add("LDR 0,0,1,11");
        // check if it's a period
        skipper.add("SMR 0,0,0,7");
        // jump if equal
        skipper.add("JZ 0,0,1,25");
        // check comma
        // load next char in storage
        skipper.add("LDR 0,0,1,11");
        // check if it's a comma
        skipper.add("SMR 0,0,0,8");
        // try next char
        skipper.add("JZ 0,0,1,21");
        skipper.add("JMA 0,0,1,29");
    }

    static {

        one.setDescription("Read 21 numbers from keyboard, compare the last " +
                "one with previous 20 numbers, print the number closest the " +
                "value of the last number. Input numbers are separated with " +
                "one ' '(space)");

        // max count
        one.putInitData(30, PROGRAM_1_MAX);
        // ' '
        one.putInitData(17, PROGRAM_1_SEPARATOR);
        one.putInitData(7, CHAR_0);
        one.putInitData(8, CHAR_EQUAL);
        // number storage start
        one.putInitData(26, 500);
        // 11 storage index
        // 12 write count
        // 13 return address for reader
        // 14 a single number
        // 15 current closest
        // 16 difference

        List<String> init = new ArrayList<>();
        List<String> loop = new ArrayList<>();
        List<String> reader = new ArrayList<>();
        List<String> assembler = new ArrayList<>();
        List<String> comparator = new ArrayList<>();
        List<String> replace = new ArrayList<>();
        List<String> printer = new ArrayList<>();

        one.putModule(Const.PROG_ADDR_POINTER, init);
        one.putModule(18, loop);
        one.putModule(19, reader);
        one.putModule(27, assembler);
        one.putModule(25, comparator);
        one.putModule(29, replace);
        one.putModule(28, printer);

        // set return address of reader to loop start
        init.add("LDR 0,0,0,18");
        init.add("STR 0,0,0,13");
        // add one more to max
        init.add("LDR 0,0,0,30");
        init.add("AIR 0,0,0,1");
        init.add("STR 0,0,0,30");
        // jump to reader
        init.add("JMA 0,0,1,19");

        // loop
        // load current write number index
        loop.add("LDR 0,0,0,12");
        // check if write number index == Max
        loop.add("SMR 0,0,0,30");
        // not zero, jump to reader
        loop.add("JNE 0,0,1,19");
        // count reached max
        // put last one into 14
        loop.add("LDR 0,0,1,11");
        loop.add("STR 0,0,0,14");
        // decrease storage index by one
        loop.add("LDR 0,0,0,11");
        loop.add("SIR 0,0,0,1");
        loop.add("STR 0,0,0,11");
        // jump to replace
        loop.add("JMA 0,0,1,29");

        // reader, read a complete number
        reader.add("IN 0,0,0,0");
        reader.add("OUT 0,0,0,1");
        // store char to EA
        reader.add("STR 0,0,0,10");
        // subtract separator, a constant
        reader.add("SMR 0,0,0,17");
        // if it's a valid char
        // to assembler
        reader.add("JNE 0,0,1,27");
        // else
        // increase write count by 1
        // then return to stored address
        // load write count
        reader.add("LDR 0,0,0,12");
        // increase count by 1
        reader.add("AIR 0,0,0,1");
        // store new write count
        reader.add("STR 0,0,0,12");
        // return to stored address
        reader.add("JMA 0,0,1,13");

        // word assembler
        // get write count
        assembler.add("LDR 0,0,0,12");
        // get storage index = write count + start
        assembler.add("AMR 0,0,0,26");
        // store storage index
        assembler.add("STR 0,0,0,11");
        // get word
        assembler.add("LDR 0,0,1,11");
        // assemble, the char to add is stored in 10
        // multiply by 10
        assembler.add("LDA 2,0,0,10");
        // rx = 0, ry = 2
        assembler.add("MLT 0,2,0,10");
        assembler.add("AMR 1,0,0,10");
        // subtract 48
        assembler.add("SMR 1,0,0,7");

        // store the assembled word
        assembler.add("STR 1,0,1,11");
        // back to reader
        assembler.add("JMA 0,0,1,19");

        //comparator
        // load current write number index
        comparator.add("LDR 0,0,0,11");
        // check if number storage index == Max
        comparator.add("SMR 0,0,0,26");
        // if zero, jump to printer
        comparator.add("JZ 0,0,1,28");
        // decrease number storage index by one
        comparator.add("LDR 0,0,0,11");
        comparator.add("SIR 0,0,0,1");
        comparator.add("STR 0,0,0,11");
        // get next number
        comparator.add("LDR 0,0,1,11");
        // get abs val of next
        comparator.add("SMR 0,0,0,14");
        comparator.add("ABS 0,0,0,0");
        // subtract the number to compare with
        comparator.add("SMR 0,0,0,16");
        // if greater or equal, do not replace
        comparator.add("JGE 0,0,1,25");
        // back to comparator start
        comparator.add("JMA 0,0,1,29");

        // replace
        // get next number
        replace.add("LDR 0,0,1,11");
        // store into 15
        replace.add("STR 0,0,0,15");
        // get the difference
        replace.add("SMR 0,0,0,14");
        replace.add("ABS 0,0,0,0");
        // store the difference
        replace.add("STR 0,0,0,16");
        // go to comparator
        replace.add("JMA 0,0,1,25");

        // printer
        printer.add("LDR 0,0,0,8");
        printer.add("OUT 0,0,0,1");
        printer.add("LDR 0,0,0,15");
        // print the final number as integer
        printer.add("OUT 0,0,1,1");
        printer.add("HLT 0,0,0,0");

    }

    private TestPrograms() {
    }

    public static Program getOne() {
        return one;
    }

    public static Program getTwo() {
        return two;
    }

}
