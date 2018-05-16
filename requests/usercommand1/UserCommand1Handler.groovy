package org.maestro.agent.ext.requests.genericrequest

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.maestro.client.notes.*;
import org.maestro.client.exchange.MaestroTopics

import org.maestro.agent.base.AbstractHandler
import groovy.json.JsonSlurper

/**
 * The process execution code here was taken from the examples provided by Joerg Mueller
 * on http://www.joergm.com/2010/09/executing-shell-commands-in-groovy. They were slightly
 * modified to adjust to the agent code
 */
class UserCommand1Handler extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserCommand1Handler.class);

    private static final RESTART = "restart";
    private static final RESTART3 = "restart3";
    private static final RESTART5 = "restart5";
    private static final SHUTDOWN1_10 = "shutdown1_1";
    private static final SHUTDOWN3_10 = "shutdown3_1";
    private static final SHUTDOWN3_30 = "shutdown3_3";
    private static final SHUTDOWN3_60 = "shutdown3_6";
    private static final SHUTDOWN3_120 = "shutdown3_12";
    private static final SHUTDOWN5_60 = "shutdown5_6";
    private static final SHUTDOWN3 = "shutdown3";

    def executeOnShell(String command) {
        return executeOnShell(command, new File(System.properties.'user.dir'))
    }

    def executeOnShell(String command, File workingDir) {
        logger.debug("Executing {}", command)

        def process = new ProcessBuilder(addShellPrefix(command))
                                        .directory(workingDir)
                                        .redirectErrorStream(true)
                                        .start()
        process.inputStream.eachLine { logger.debug("Subprocess output: {}", it) }
        process.waitFor();

        return process.exitValue()
    }

    def addShellPrefix(String command) {
        String[] commandArray = new String[3]

        commandArray[0] = "sh"
        commandArray[1] = "-c"
        commandArray[2] = command
        return commandArray
    }

    def executeCommand(String command) {
        if (executeOnShell(command) != 0) {
            logger.warn("Unable to execute the user's command: " + command)
            // this.getClient().notifyFailure("Unable to execute user's command: " + command)

            return null
        }
        this.getClient().replyOk()
        logger.info("Agent executed command successfully")
    }

    @Override
    Object handle() {
        String logDir = System.getProperty("maestro.log.dir")
        int sleepTime = 0;
        String command = "";

        logger.info("Erasing old data")
        "rm -rf ${logDir}/agent".execute();

        UserCommand1Request request = (UserCommand1Request) getNote()
        String payload = request.getPayload()

        try {
            switch(payload) {
              case RESTART:
                  logger.info("Going to execute restart command")
                  command = "sudo systemctl restart qdrouterd"
                  executeCommand(command);

              break
              case RESTART3:
                  logger.info("Going to execute restart command after 180 seconds")
                  command = "sudo systemctl restart qdrouterd"
                  sleep(180000);
                  executeCommand(command);

              break
              case RESTART5:
                  logger.info("Going to execute restart command after 300 seconds")
                  command = "ssudo ystemctl restart qdrouterd"
                  sleep(300000);
                  executeCommand(command);

              break
              case SHUTDOWN1_10:
                  logger.info("Going to execute stop command after 60 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(60000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 10 seconds")
                  sleep(10000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);

              break
              case SHUTDOWN3_10:
                  logger.info("Going to execute stop command after 180 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(180000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 10 seconds")
                  sleep(10000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);
              break
              case SHUTDOWN3_30:
                  logger.info("Going to execute stop command after 180 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(180000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 30 seconds")
                  sleep(30000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);

              break
              case SHUTDOWN3_60:
                  logger.info("Going to execute stop command after 180 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(180000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 60 seconds")
                  sleep(60000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);

              break
              case SHUTDOWN3_120:
                  logger.info("Going to execute stop command after 180 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(180000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 120 seconds")
                  sleep(120000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);

              break
              case SHUTDOWN3:
                  logger.info("Going to execute stop command after 180 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(180000);
                  executeCommand(command);

              break
              case SHUTDOWN5_60:
                  logger.info("Going to execute stop command after 300 seconds")
                  command = "sudo systemctl stop qdrouterd"
                  sleep(300000);
                  executeCommand(command);
                  logger.info("Going to execute start command after 60 seconds")
                  sleep(60000)
                  command = "sudo systemctl start qdrouterd"
                  executeCommand(command);

              break
              default:

              break
            }
        }
        finally{
            logger.info("Finish user command handler")
        }
        return null
    }
}
