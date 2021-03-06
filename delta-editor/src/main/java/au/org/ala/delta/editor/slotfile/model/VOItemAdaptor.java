/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.editor.slotfile.model;

import au.org.ala.delta.editor.slotfile.AttrChunk;
import au.org.ala.delta.editor.slotfile.Attribute;
import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.TextType;
import au.org.ala.delta.editor.slotfile.VOCharBaseDesc;
import au.org.ala.delta.editor.slotfile.VOImageHolderDesc;
import au.org.ala.delta.editor.slotfile.VOItemDesc;
import au.org.ala.delta.model.AttributeFactory;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.impl.AttributeData;
import au.org.ala.delta.model.impl.ItemData;
import au.org.ala.delta.util.Pair;
import au.org.ala.delta.util.Utils;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;

/**
 * Adapts a slot file VOItemDesc to the model Item interface.
 */
public class VOItemAdaptor extends ImageHolderAdaptor implements ItemData {

	private VOItemDesc _voItemDesc;
	private DeltaVOP _vop;
	
	public VOItemAdaptor(DeltaVOP vop, VOItemDesc voItem) {
		_vop = vop;
		_voItemDesc = voItem;
	}
	
	@Override
	public int getNumber() {
		return _vop.getDeltaMaster().itemNoFromUniId(_voItemDesc.getUniId());
	}

	@Override
	public void setNumber(int number) {
		throw new RuntimeException("Internal Error: Attempt to set number on VOItemAdaptor!");
	}
	
	public void setDescription(String itemName) {		
		synchronized (_vop) {
			String oldDescription = getDescription();
			
			Attribute nameAttribute = new Attribute(VOItemDesc.VOUID_NAME);
			nameAttribute.insert(0, new AttrChunk(itemName));
			_voItemDesc.writeAttribute(nameAttribute);
			
			if (!Utils.RTFToANSI(itemName).equals(oldDescription)) {
				_vop.deleteFromNameList(_voItemDesc);
				_vop.insertInNameList(_voItemDesc);
			}
		}
	}


	public String getDescription() {
		synchronized (_vop) {
			return _voItemDesc.readAttributeAsText(VOItemDesc.VOUID_NAME, TextType.RTF);
		}
	}
	
	public String getUnformattedDescription() {
		synchronized (_vop) {
			return _voItemDesc.getAnsiName();	
		}
	}


	@Override
	public List<au.org.ala.delta.model.Attribute> getAttributes() {
		
		throw new NotImplementedException();
	}

	@Override
	public au.org.ala.delta.model.Attribute getAttribute(Character character, Item item) {
		synchronized (_vop) {
			if (character == null) {
				return null;
			}
			
			AttributeData impl = new VOAttributeAdaptor(_voItemDesc, getVOCharBaseDesc(character));
			return AttributeFactory.newAttribute(character, impl);
		}
	}

	/**
	 * This method does nothing - the VOItemAdaptor reads from the slot file and creates Attributes
	 * when requested by getAttribute. 
	 */
	@Override
	public void addAttribute(Character character, au.org.ala.delta.model.Attribute attribute) {
		synchronized (_vop) {
			Attribute slotFileAttribute = new Attribute(attribute.getValueAsString(), getVOCharBaseDesc(character));
			_voItemDesc.writeAttribute(slotFileAttribute);
		}
	}
	
	private VOCharBaseDesc getVOCharBaseDesc(Character character) {
		return ((VOCharacterAdaptor)character.getImpl()).getCharBaseDesc();
	}

	@Override
	public boolean isVariant() {
		synchronized (_vop) {
			return _voItemDesc.isVariant();
		}
	}

	@Override
	public void setVariant(boolean variant) {
		synchronized (_vop) {
			_voItemDesc.setVariant(variant);
		}
	}
	
	public VOItemDesc getItemDesc() {
		return _voItemDesc;
	}

	@Override
	protected VOImageHolderDesc getImageHolder() {
		return _voItemDesc;
	}

	@Override
	protected DeltaVOP getVOP() {
		return _vop;
	}

    @Override
    public List<Pair<String, String>> getLinkFiles() {
        throw new NotImplementedException();
    }

    @Override
    public void setLinkFiles(List<Pair<String, String>> linkFiles) {
        throw new NotImplementedException();
    }
   
}
