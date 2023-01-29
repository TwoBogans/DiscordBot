package au.twobeetwotee.discord.command;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.util.Util;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class CommandManager {

    @Getter
    private final HashSet<Command> commands = new HashSet<>();

    public CommandManager() {
        this.commands.addAll(loadCommands());
        Main.getJda().addEventListener(new CommandListener(this));
    }
    
    private HashSet<Command> loadCommands() {
        var commands = new HashSet<Command>();
        var reflections = new Reflections(Command.class.getPackage().getName());
        var basePath = "au.twobeetwotee.discord.command.commands.%s";
        for (Class<? extends Command> cls : reflections.getSubTypesOf(Command.class)) {
            for (Command.Category category : Command.Category.values()) {
                var commandsPackage = basePath.formatted(category.name().toLowerCase(Locale.ROOT));
                if (cls.getPackage().getName().equals(commandsPackage)
                        && !Modifier.isAbstract(cls.getModifiers())) {
                    commands.add(Util.createNewClassInstance(cls, new Class[]{}, new Object[]{}));
                }
            }
        }
        return commands;
    }

    @RequiredArgsConstructor
    public static class CommandListener extends ListenerAdapter {
        @NonNull private CommandManager manager;

        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
            manager.commands.forEach(command -> {
                if (event.getName().equalsIgnoreCase(command.getName())) {
                    if (command.getCategory() == Command.Category.MAIN &&
                            Objects.requireNonNull(event.getGuild()).getIdLong() != Main.getConfig().getMainGuild()) {
                        event.reply("discord.gg/popbob only!")
                                .setEphemeral(true)
                                .queue();
                        return;
                    }

                    command.onCommand(event);
                }
            });
        }
    }
}
