/******************************************************************************
 * Copyright 2009-2018 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.exactpro.sf.services.fix;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.exactpro.sf.common.messages.structures.IDictionaryStructure;
import com.exactpro.sf.common.messages.structures.loaders.XmlDictionaryStructureLoader;

import quickfix.DataDictionaryProvider;
import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.Session;
import quickfix.field.ApplVerID;
import quickfix.field.SessionRejectReason;
import quickfix.fix50.MessageFactory;


public class QFJDictionaryAdapterTest {
    private Session session;
    private QFJDictionaryAdapter qfjDictionaryAdapter;

    @Before
    public void init() throws IOException {
        this.session = mock(Session.class);
        DataDictionaryProvider provider = mock(DataDictionaryProvider.class);
        when(session.getDataDictionaryProvider()).thenReturn(provider);
        when(session.getMessageFactory()).thenReturn(new MessageFactory());
        when(session.getTargetDefaultApplicationVersionID()).thenReturn(new ApplVerID(ApplVerID.FIX50SP2));

        IDictionaryStructure dictionaryStructure;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("dictionary/FIX50.TEST.xml")) {
        	dictionaryStructure = new XmlDictionaryStructureLoader().load(in);
    	}

        this.qfjDictionaryAdapter = new QFJDictionaryAdapter(dictionaryStructure);
        when(provider.getApplicationDataDictionary(new ApplVerID(ApplVerID.FIX50SP2))).thenReturn(qfjDictionaryAdapter);
    }


    @Test
    public void testValidateRequiredTags() throws Exception {
        try {
            String messageString =  "8=FIXT.1.19=24235=X49=TOR56=ft0134=852=20150529-11:30:20.596262=14328989480720000002-MBO1091=Y235=XD268=1279=0453=1448=25948=322=310=202";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
            Assert.fail("Failed negative test to validate required tags, no expected error");
        } catch (FieldException e){
            Assert.assertEquals(55, e.getField());
            Assert.assertEquals(SessionRejectReason.REQUIRED_TAG_MISSING, e.getSessionRejectReason());
        }
    }

    @Test
    public void testComponentInGroupDoesNotAddNullTag() throws Exception {
        String messageString = "8=FIXT.1.19=9435=RG34=321749=TEST_152=20200428-16:47:06.20456=FMSSTEST1777=1448=mb447=D452=311=12310=202";
        Message message = MessageUtils.parse(session, messageString);
        qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
        qfjDictionaryAdapter.validate(message, false);
    }

    @Test
    public void testMissedRequiredTagInGroup() throws Exception {
        try {
            String messageString = "8=FIXT.1.19=8835=RG34=321749=TEST_152=20200428-16:47:06.20456=FMSSTEST1777=1448=mb452=311=12310=172";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
        } catch (FieldException e) {
            Assert.assertEquals(447, e.getField());
            Assert.assertEquals(SessionRejectReason.REQUIRED_TAG_MISSING, e.getSessionRejectReason());
        }
    }

    @Test
    public void testMissedRequiredTagInComponentInGroup() throws Exception {
        try {
            String messageString = "8=FIXT.1.19=8735=RG34=321749=TEST_152=20200428-16:47:06.20456=FMSSTEST1777=1448=mb447=D452=310=150";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
        } catch (FieldException e) {
            Assert.assertEquals(11, e.getField());
            Assert.assertEquals(SessionRejectReason.REQUIRED_TAG_MISSING, e.getSessionRejectReason());
        }
    }

    @Test
    public void testValidateInsertGroup() throws Exception {
        try {
            String messageString =  "8=FIXT.1.19=24235=X49=TOR56=ft0134=852=20150529-11:30:20.596262=14328989480720000002-MBO1091=Y235=XD268=1279=0453=1448=25948=322=355=310=165";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSingleGroupInOtherGroup() throws Exception {
        try {
            String messageString = "8=FIXT.1.19=00010135=CM49=AAA56=EEEE134=252=20160222-16:06:47.7571677=11671=11691=EEE11669=11529=11530=41534=11535=110=25";
            Message msg = new Message();
            msg.fromString(messageString, qfjDictionaryAdapter, true);
//            Message message = MessageUtils.parse(this.session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(false);
            qfjDictionaryAdapter.validate(msg, false);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAllowOtherValues() throws Exception {
        try {
            String messageString =  "8=FIXT.1.19=23535=X49=TOR56=ft0134=852=20150529-11:30:20.596262=14328989480720000002-MBO234=000.1235=XD268=1279=0453=1448=25948=322=355=310=11";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        try {
            String messageString =  "8=FIXT.1.19=23535=X49=TOR56=ft0134=852=20150529-11:30:20.596262=14328989480720000002-MBO234=XD235=0.001268=1279=0453=1448=25948=322=355=310=11";
            Message message = MessageUtils.parse(session, messageString);
            qfjDictionaryAdapter.setAllowUnknownMessageFields(true);
            qfjDictionaryAdapter.validate(message, false);
        } catch (IncorrectTagValue e) {
            Assert.assertEquals(235, e.field);
        }
    }

}