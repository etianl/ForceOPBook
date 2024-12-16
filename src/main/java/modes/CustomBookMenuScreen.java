package modes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Unique;

import static modes.bookModes.ForceOP;

public class CustomBookMenuScreen extends Screen {
    private final Screen parent;

    public CustomBookMenuScreen(Screen parent) {
        super(Text.literal("Custom Menu"));
        this.parent = parent;
    }
    @Unique
    private final MinecraftClient mc = MinecraftClient.getInstance();
    @Unique
    private bookModes currentMode = ForceOP;
    @Unique
    private TextFieldWidget command1Field;
    private TextFieldWidget AuthorField;
    private TextFieldWidget TitleField;
    private TextFieldWidget TextField;
    @Unique
    private CyclingButtonWidget<bookModes> modeButton;
    @Override
    protected void init() {
        super.init();
        int startY = height / 4;
        int spacing = 18;

        modeButton = CyclingButtonWidget.<bookModes>builder(mode -> Text.literal(mode.name()))
                .values(bookModes.values())
                .initially(currentMode)
                .build(230, startY + spacing, 170, 18, Text.literal("Cmd Mode"));
        addDrawableChild(modeButton);

        addDrawableChild(new ButtonWidget.Builder(Text.literal("------>Create OP Book<------"), button -> createOpSign())
                .position(230, startY + 185)
                .size(170, 18)
                .build()
        );

        addDrawableChild(new ButtonWidget.Builder(Text.literal("BACK"), button -> mc.setScreen(parent))
                .position(180, startY + 185)
                .size(50, 18)
                .build()
        );

        int yPos = startY + spacing + spacing * (5);
        addDrawableChild(new ButtonWidget.Builder(Text.literal("Title:"), button -> {})
                .position(230, yPos-36)
                .size(45, 18)
                .build());
        TextFieldWidget titleField = new TextFieldWidget(textRenderer, 230 + 50, yPos-36, 120, 18, Text.literal("Title"));
        titleField.setMaxLength(256);
        titleField.setChangedListener(newText -> onCommandChanged(2, newText));
        addDrawableChild(titleField);
        TitleField = titleField;

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Author:"), button -> {})
                .position(230, yPos-18)
                .size(45, 18)
                .build());
        TextFieldWidget authorField = new TextFieldWidget(textRenderer, 230 + 50, yPos-18, 120, 18, Text.literal("Author"));
        authorField.setMaxLength(256);
        authorField.setChangedListener(newText -> onCommandChanged(1, newText));
        addDrawableChild(authorField);
        AuthorField = authorField;

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Cmd:"), button -> {})
                .position(230, yPos)
                .size(45, 18)
                .build());

        TextFieldWidget commandField = new TextFieldWidget(textRenderer, 230 + 50, yPos, 120, 18, Text.literal("Command"));
        commandField.setMaxLength(256);
        commandField.setChangedListener(newText -> onCommandChanged(0, newText));
        addDrawableChild(commandField);
        command1Field = commandField;

        addDrawableChild(new ButtonWidget.Builder(Text.literal("BookText:"), button -> {})
                .position(230, yPos+18)
                .size(45, 18)
                .build());
        TextFieldWidget textField = new TextFieldWidget(textRenderer, 230 + 50, yPos+18, 120, 18, Text.literal("BookText"));
        textField.setMaxLength(256);
        textField.setChangedListener(newText -> onCommandChanged(3, newText));
        addDrawableChild(textField);
        TextField = textField;

        addDrawableChild(new ButtonWidget.Builder(Text.literal("§n§lCommand Options:"), button -> {})
                .position(230, startY)
                .size(170, 18)
                .build());
    }
    @Unique
    private String command1Value = "";
    @Unique
    private String AuthorValue = "";
    @Unique
    private String TitleValue = "";
    @Unique
    private String TextValue = "";
    @Unique
    private TextFieldWidget[] getCustomFields() {
        return new TextFieldWidget[]{
                command1Field, AuthorField, TitleField, TextField
        };
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clickedCustomField = false;
        for (TextFieldWidget field : getCustomFields()) {
            if (field == null) return super.mouseClicked(mouseX, mouseY, button);
            if (field.isMouseOver(mouseX, mouseY)) {
                field.setFocused(true);
                clickedCustomField = true;
            } else {
                field.setFocused(false);
            }
        }

        return clickedCustomField || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (TextFieldWidget field : getCustomFields()) {
            if (field == null) return super.keyPressed(keyCode, scanCode, modifiers);
            if (field.isFocused()) {
                return field.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (TextFieldWidget field : getCustomFields()) {
            if (field == null) return super.charTyped(chr, modifiers);
            if (field.isFocused()) {
                return field.charTyped(chr, modifiers);
            }
        }

        return super.charTyped(chr, modifiers);
    }
    @Unique
    private void onCommandChanged(int index, String newText) {
        switch (index) {
            case 0 -> command1Value = newText;
            case 1 -> AuthorValue = newText;
            case 2 -> TitleValue = newText;
            case 3 -> TextValue = newText;
        }
    }
    @Unique
    private void createOpSign() {
        assert mc.player != null;
        if (!mc.player.getAbilities().creativeMode) {
            mc.inGameHud.getChatHud().addMessage(Text.literal("You need creative mode to make the book."));
            return;
        }

        bookModes selectedMode = modeButton.getValue();

        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        String commandValue1 = command1Value;
        String authorValue = AuthorValue;
        String titleValue = TitleValue;
        String textValue = TextValue;

        NbtCompound nbt = new NbtCompound();

        nbt.putString("title", titleValue);
        nbt.putString("author", authorValue);
        NbtList pages = new NbtList();
        if (selectedMode == ForceOP){
            String pageContent = "{\"text\":\""+textValue+"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             "
                    +"\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/op "+mc.player.getName().getLiteralString()+"\"}}";
            pages.add(NbtString.of(pageContent));
        } else {
            String pageContent = "{\"text\":\""+textValue+"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             "
                    +"\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+commandValue1+"\"}}";
            pages.add(NbtString.of(pageContent));
        }
        nbt.put("pages", pages);

        stack.setNbt(nbt);

        assert mc.interactionManager != null;
        mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
        mc.inGameHud.getChatHud().addMessage(Text.literal("OP Book created. Give it to an operator and have them click the first page to execute the command."));
        switch (selectedMode) {
            case ForceOP -> mc.inGameHud.getChatHud().addMessage(Text.literal("ForceOP mode selected. Cmd 1 will not be executed!"));
        }
        mc.setScreen(parent);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        mc.setScreen(parent);
    }
}