package com.Hiag0.clearlag.messages;

import com.hypixel.hytale.server.core.Message;
import java.awt.Color;

public class Messages {

    public static final String PREFIX_TEXT = "[ClearLag] ";

    public static final Message STARTUP = Message.raw(PREFIX_TEXT + "ClearLag iniciado. Limpeza agendada.")
            .color(Color.GREEN);

    public static Message scheduleInfo(int minutes) {
        return Message.raw(PREFIX_TEXT + "Limpeza a cada " + minutes + " minutos.").color(Color.GREEN);
    }

    public static Message warning(int seconds) {
        return Message.raw(PREFIX_TEXT + "Limpeza em " + seconds + " segundos...").color(Color.YELLOW);
    }

    public static final Message CLEANUP_STARTED = Message.raw(PREFIX_TEXT + "Executando limpeza agendada...")
            .color(Color.ORANGE);

    public static Message nextCleanup(int minutes) {
        return Message.raw(PREFIX_TEXT + "Proxima limpeza em " + minutes + " minutos.").color(Color.GREEN);
    }

    public static Message manualCleanupStart(String worldName) {
        return Message.raw(PREFIX_TEXT + "Iniciando limpeza manual no mundo: " + worldName).color(Color.YELLOW);
    }

    public static Message cleanupFinished(String worldName, int count) {
        return Message.raw(PREFIX_TEXT + "Varredura finalizada no mundo " + worldName + ". Itens removidos: " + count)
                .color(Color.GREEN);
    }

    public static Message logError(String error) {
        return Message.raw(PREFIX_TEXT + "Erro ao gravar debug log: " + error).color(Color.RED);
    }
}
