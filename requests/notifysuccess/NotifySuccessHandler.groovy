package org.maestro.agent.ext.requests.notifysuccess

import org.maestro.agent.base.AbstractHandler

class NotifySuccessHandler extends AbstractHandler {

    @Override
    Object handle() {
         this.getClient().notifySuccess("Agent executed command successfully")
    }
}
