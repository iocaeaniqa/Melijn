package me.melijn.melijnbot.commands.image

import me.melijn.melijnbot.commandutil.image.ImageCommandUtil
import me.melijn.melijnbot.enums.DiscordSize
import me.melijn.melijnbot.internals.command.AbstractCommand
import me.melijn.melijnbot.internals.command.CommandCategory
import me.melijn.melijnbot.internals.command.ICommandContext
import me.melijn.melijnbot.internals.utils.ImageType
import me.melijn.melijnbot.internals.utils.ImageUtils
import me.melijn.melijnbot.internals.utils.getIntegerFromArgNMessage
import net.dv8tion.jda.api.Permission

class RerenderGifCommand : AbstractCommand("command.rerendergif") {

    init {
        id = 151
        name = "rerenderGif"
        aliases = arrayOf("rerender")
        discordChannelPermissions = arrayOf(Permission.MESSAGE_ATTACH_FILES)
        commandCategory = CommandCategory.DEVELOPER
    }

    override suspend fun execute(context: ICommandContext) {
        val acceptTypes = setOf(ImageType.GIF)
        val image = ImageUtils.getImageBytesNMessage(context, 0, DiscordSize.X1024, acceptTypes) ?: return
        val centiSecondDelay = if (context.args.size > 1) getIntegerFromArgNMessage(context, 1, 2) ?: return else null

        ImageCommandUtil.applyGifImmutableFrameModification(context, image, {}, { delay ->
            centiSecondDelay ?: delay
        })
    }
}