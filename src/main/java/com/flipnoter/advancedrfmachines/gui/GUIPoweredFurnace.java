package com.flipnoter.advancedrfmachines.gui;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.guicontainer.ContainerFurnaceGenerator;
import com.flipnoter.advancedrfmachines.guicontainer.ContainerPoweredFurnace;
import com.flipnoter.advancedrfmachines.tileEntites.FurnaceGeneratorTileEntity;
import com.flipnoter.advancedrfmachines.tileEntites.PoweredFurnaceTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;


/**
 * Created by Connor on 3/6/2016.
 */
public class GUIPoweredFurnace extends GuiContainer {

    private IInventory playerInv;
    private PoweredFurnaceTileEntity te;

    public GUIPoweredFurnace(IInventory playerInv, PoweredFurnaceTileEntity te) {

        super(new ContainerPoweredFurnace(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

        xSize = 176;
        ySize = 166;

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation(advancedrfmachines.MODID + ":textures/gui/container/Powered_Furnace.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        float rf = (float)te.getEnergyStored(EnumFacing.NORTH) / (float)te.getMaxEnergyStored(EnumFacing.NORTH);
        drawTexturedModalRect(guiLeft + 8, guiTop + 8 + (42 - (int)(rf * 42)), 177, 42 - (int)(rf * 42), 16, (int)(rf * 42));

        float time = (float)te.getTimeRemaining() / (float)te.getBurnTime();
        drawTexturedModalRect(guiLeft + 79, guiTop + 35, 193, 0, 24 - (int)(time * 24), 16);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = te.getDisplayName().getUnformattedText();
        fontRendererObj.drawString(s, 88 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);

    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);
        drawHoverText(x - guiLeft, y - guiTop);

    }

    private void drawHoverText(int x, int y) {

        if(x > 7 && x < 25 && y > 7 && y < 50) {

            ArrayList<String> internal = new ArrayList<String>();
            internal.add("Stored RF");
            internal.add("" + EnumChatFormatting.RED + te.getEnergyStored(EnumFacing.NORTH) + "/" + te.getMaxEnergyStored(EnumFacing.NORTH));
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            drawHoveringText(internal, x + guiLeft, y + guiTop, fontRendererObj);
            GL11.glPopAttrib();

        }
    }
}
