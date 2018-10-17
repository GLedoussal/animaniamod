package com.animania.client.render.sheep;

import org.lwjgl.opengl.GL11;

import com.animania.client.models.sheep.ModelMerinoEwe;
import com.animania.client.models.sheep.ModelSuffolkEwe;
import com.animania.client.render.layer.LayerBlinking;
import com.animania.common.entities.sheep.EntityAnimaniaSheep;
import com.animania.common.entities.sheep.EntityEweSuffolk;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEweSuffolk<T extends EntityEweSuffolk> extends RenderLiving<T>
{
	public static final Factory FACTORY = new Factory();
	private static final String modid = "animania", SheepBaseDir = "textures/entity/sheep/";

	private static final ResourceLocation[] SHEEP_TEXTURES = new ResourceLocation[] { new ResourceLocation(RenderEweSuffolk.modid, RenderEweSuffolk.SheepBaseDir + "sheep_suffolk_" + "white_ewe.png"), new ResourceLocation(RenderEweSuffolk.modid, RenderEweSuffolk.SheepBaseDir + "sheep_suffolk_" + "brown_ewe.png") };

	private static final ResourceLocation SHEEP_TEXTURE_BLINK = new ResourceLocation("animania:textures/entity/sheep/sheep_blink.png");
	private static final ResourceLocation[] SHEEP_TEXTURES_SHEARED = new ResourceLocation[] { new ResourceLocation(RenderEweSuffolk.modid, RenderEweSuffolk.SheepBaseDir + "sheep_suffolk_" + "white_ewe_sheared.png"), new ResourceLocation(RenderEweSuffolk.modid, RenderEweSuffolk.SheepBaseDir + "sheep_suffolk_" + "brown_ewe_sheared.png") };

	public RenderEweSuffolk(RenderManager rm)
	{
		super(rm, new ModelSuffolkEwe(), 0.5F);
		this.addLayer(new LayerBlinking(this, SHEEP_TEXTURE_BLINK, 0x1D1D1D));
	}

	protected void preRenderScale(EntityEweSuffolk entity, float f)
	{
		GL11.glScalef(0.64F, 0.64F, 0.64F);
		GL11.glTranslatef(0f, 0f, -0.5f);
		boolean isSleeping = false;
		EntityAnimaniaSheep entitySheep = (EntityAnimaniaSheep) entity;
		if (entitySheep.getSleeping())
		{
			isSleeping = true;
		}

		if (isSleeping)
		{
			this.shadowSize = 0;
			float sleepTimer = entitySheep.getSleepTimer();
			if (sleepTimer > -0.55F)
			{
				sleepTimer = sleepTimer - 0.01F;
			}
			entity.setSleepTimer(sleepTimer);

			GlStateManager.translate(-0.25F, entity.height - 1.05F - sleepTimer, -0.25F);
			GlStateManager.rotate(6.0F, 0.0F, 0.0F, 1.0F);
		}
		else
		{
			this.shadowSize = 0.5F;
			entitySheep.setSleeping(false);
			entitySheep.setSleepTimer(0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		if (entity.posX == -1 && entity.posY == -1 && entity.posZ == -1)
		{
			return SHEEP_TEXTURES[0];
		}

		if (!entity.getSheared())
		{
			return this.SHEEP_TEXTURES[entity.getColorNumber()];
		}
		else
		{
			return this.SHEEP_TEXTURES_SHEARED[entity.getColorNumber()];
		}
	}

	@Override
	protected void preRenderCallback(T entityliving, float f)
	{
		this.preRenderScale(entityliving, f);
		if (entityliving.hasCustomName() && "jeb_".equals(entityliving.getCustomNameTag()) && entityliving.isDyeable())
		{
			int i1 = 25;
			int i = entityliving.ticksExisted / 25 + entityliving.getEntityId();
			int j = EnumDyeColor.values().length;
			int k = i % j;
			int l = (i + 1) % j;
			float q = ((float) (entityliving.ticksExisted % 25) + f) / 25.0F;
			float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
			float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
			((ModelSuffolkEwe) this.mainModel).setWoolColor(afloat1[0] * (1.0F - q) + afloat2[0] * q, afloat1[1] * (1.0F - q) + afloat2[1] * q, afloat1[2] * (1.0F - q) + afloat2[2] * q);
		}
		else
		{
			float[] rgb = entityliving.getDyeColor().getColorComponentValues();
			((ModelSuffolkEwe) this.mainModel).setWoolColor(rgb[0], rgb[1], rgb[2]);
		}
	}

	static class Factory<T extends EntityEweSuffolk> implements IRenderFactory<T>
	{
		@Override
		public Render<? super T> createRenderFor(RenderManager manager)
		{
			return new RenderEweSuffolk(manager);
		}

	}
}
