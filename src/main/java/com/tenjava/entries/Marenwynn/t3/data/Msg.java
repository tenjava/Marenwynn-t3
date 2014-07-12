package com.tenjava.entries.Marenwynn.t3.data;

import org.bukkit.command.CommandSender;

import com.tenjava.entries.Marenwynn.t3.Util;

public enum Msg {

    PREFIX_ERROR("&cError: &f");

    private final String defaultMsg;

    Msg(String defaultMsg) {
        this.defaultMsg = defaultMsg;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }

    @Override
    public String toString() {
        return Util.color(Data.getMsg(this));
    }

    public void sendErrorTo(CommandSender sender) {
        sender.sendMessage(PREFIX_ERROR.toString() + toString());
    }

    public void sendErrorTo(CommandSender sender, Object... args) {
        sender.sendMessage(PREFIX_ERROR.toString() + String.format(toString(), args));
    }

    public void sendTo(CommandSender sender) {
        sender.sendMessage(toString());
    }

    public void sendTo(CommandSender sender, Object... args) {
        sender.sendMessage(String.format(toString(), args));
    }

}