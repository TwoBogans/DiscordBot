package au.twobeetwotee.discord.command;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;


public abstract class Command extends CommandDataImpl {
    @Getter private final String name;
    @Getter private final String desc;
    @Getter private final Category category;

    @Getter @Setter
    private boolean mainGuildOnly;

    public Command(@NonNull String name, @NonNull String description, @NonNull Category category) {
        super(name, description);
        this.name = name;
        this.desc = description;
        this.category = category;
    }

    public abstract void onCommand(SlashCommandInteractionEvent event);

    public enum Category {
        INFO, // General Bot Commands
        SERVER, // api.2b2t.au Commands
        ADMIN, // Admin Bot Commands
        FUN, // Random Fun Stuff soon™
        MAIN // discord.gg/popbob only commands
    }
}
