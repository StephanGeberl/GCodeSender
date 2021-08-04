/**
 * Throws an exception if the command is too long.
 */
package com.geberl.gcodesender.gcode.processors;

import java.util.ArrayList;
import java.util.List;

import com.geberl.gcodesender.gcode.GcodeState;
import com.geberl.gcodesender.gcode.util.GcodeParserException;

/**
 *
 * @author wwinder
 */
public class CommandLengthProcessor implements CommandProcessor {
    final private int length;
    public CommandLengthProcessor(int length) {
        this.length = length;
    }

    @Override
    public String getHelp() {
        return "Max command length\\: Maximum length of a command before an error is triggered." + "\n" +
                "Max command length"
                + ": " + length;
    }

    @Override
    public List<String> processCommand(String command, GcodeState state) throws GcodeParserException {
        if (command.length() > length)
            throw new GcodeParserException("Command '" + command + "' is longer than " + length + " characters.");

        List<String> ret = new ArrayList<>();
        ret.add(command);
        return ret;
    }
    
}
