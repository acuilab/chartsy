package org.chartsy.main.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.chartsy.main.ChartFrame;
import org.chartsy.main.chart.Indicator;
import org.chartsy.main.chart.Overlay;
import org.chartsy.main.templates.Template;
import org.chartsy.main.utils.FileUtils;
import org.chartsy.main.utils.XMLUtil;
import org.openide.filesystems.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 模板管理器
 * @author Viorel
 */
/*
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<templates>
    <template name="Simple Chart">
        <chart name="Candle Stick">
            <properties>
                <property colorvalue="46,52,54" name="axisColor"/>
                <property intvalue="0" name="axisStrokeIndex"/>
                <property doublevalue="8.0" name="barWidth"/>
                <property colorvalue="46,52,54" name="barColor"/>
                <property intvalue="0" name="barStrokeIndex"/>
                <property booleanvalue="true" name="barVisibility"/>
                <property colorvalue="239,41,41" name="barDownColor"/>
                <property booleanvalue="true" name="barDownVisibility"/>
                <property colorvalue="115,210,22" name="barUpColor"/>
                <property booleanvalue="true" name="barUpVisibility"/>
                <property colorvalue="238,238,236" name="gridHorizontalColor"/>
                <property intvalue="0" name="gridHorizontalStrokeIndex"/>
                <property booleanvalue="true" name="gridHorizontalVisibility"/>
                <property colorvalue="238,238,236" name="gridVerticalColor"/>
                <property intvalue="0" name="gridVerticalStrokeIndex"/>
                <property booleanvalue="true" name="gridVerticalVisibility"/>
                <property colorvalue="255,255,255" name="backgroundColor"/>
                <property fontvalue="Dialog,0,12" name="font"/>
                <property colorvalue="46,52,54" name="fontColor"/>
            </properties>
        </chart>
        <overlays>
            <overlay name="SMA">
                <properties>
                    <property intvalue="50" name="period"/>
                    <property name="price" stringvalue="Close"/>
                    <property name="label" stringvalue="SMA"/>
                    <property booleanvalue="true" name="marker"/>
                    <property colorvalue="28,63,71" name="color"/>
                    <property intvalue="0" name="strokeIndex"/>
                </properties>
            </overlay>
            <overlay name="SMA">
                <properties>
                    <property intvalue="100" name="period"/>
                    <property name="price" stringvalue="Close"/>
                    <property name="label" stringvalue="SMA"/>
                    <property booleanvalue="true" name="marker"/>
                    <property colorvalue="2,81,241" name="color"/>
                    <property intvalue="0" name="strokeIndex"/>
                </properties>
            </overlay>
            <overlay name="SMA">
                <properties>
                    <property intvalue="200" name="period"/>
                    <property name="price" stringvalue="Close"/>
                    <property name="label" stringvalue="SMA"/>
                    <property booleanvalue="true" name="marker"/>
                    <property colorvalue="184,81,24" name="color"/>
                    <property intvalue="0" name="strokeIndex"/>
                </properties>
            </overlay>
        </overlays>
        <indicators>
            <indicator name="RSI">
                <properties>
                    <property intvalue="14" name="period"/>
                    <property name="label" stringvalue="RSI"/>
                    <property intvalue="0" name="sourceDataset"/>
                    <property intvalue="9" name="hmaPeriod"/>
                    <property booleanvalue="true" name="marker"/>
                    <property colorvalue="78,154,6" name="color"/>
                    <property intvalue="0" name="strokeIndex"/>
                    <property colorvalue="78,154,6" name="insideColor"/>
                    <property intvalue="25" name="insideAlpha"/>
                    <property booleanvalue="true" name="insideVisibility"/>
                </properties>
            </indicator>
            <indicator name="MACD">
                <properties>
                    <property intvalue="12" name="fast"/>
                    <property intvalue="26" name="slow"/>
                    <property intvalue="9" name="smooth"/>
                    <property name="label" stringvalue="MACD"/>
                    <property booleanvalue="true" name="marker"/>
                    <property colorvalue="238,238,236" name="zeroLineColor"/>
                    <property intvalue="0" name="zeroLineStrokeIndex"/>
                    <property booleanvalue="true" name="zeroLineVisibility"/>
                    <property colorvalue="115,136,10" name="histogramPositiveColor"/>
                    <property colorvalue="204,0,0" name="histogramNegativeColor"/>
                    <property colorvalue="92,53,102" name="signalColor"/>
                    <property intvalue="0" name="signalStrokeIndex"/>
                    <property colorvalue="78,154,6" name="macdColor"/>
                    <property intvalue="0" name="macdStrokeIndex"/>
                </properties>
            </indicator>
        </indicators>
    </template>
</templates>
*/
public class TemplateManager {

    private static TemplateManager instance;
    private HashMap<String, Template> templates;
    private String defTemp;
    private File defaultTemplate;                   // 默认模板文件
    private File templatesXML;                      // 模板文件

    public static TemplateManager getDefault() {
        if (instance == null) {
            instance = new TemplateManager();
        }
        return instance;
    }

    private TemplateManager() {
        defaultTemplate = FileUtils.templatesFile("default.xml");   // 默认模板文件default.xml
        templatesXML = FileUtils.templatesFile("templates.xml");    // 模板文件templates.xml，模板文件里可以包含多个模板
        templates = new HashMap<String, Template>();
        if (!templatesXML.exists()) {
            try {
                FileUtil.copy(
                        Template.class.getResourceAsStream("default.xml"),
                        FileUtil.createData(defaultTemplate).getOutputStream());
                FileUtil.copy(
                        Template.class.getResourceAsStream("templates.xml"),
                        FileUtil.createData(templatesXML).getOutputStream());
            } catch (IOException ex) {
                XMLUtil.createXMLDocument(defaultTemplate);
                XMLUtil.createXMLDocument(templatesXML, XMLUtil.TEMPLATES_NODE);
            }
            initTemplates();
        } else {
            initTemplates();
        }
    }

    public String getDefaultTemplate() {
        return defTemp;
    }

    public void setDefaultTemplate(String name) {
        defTemp = name;
        Document document = XMLUtil.loadXMLDocument(defaultTemplate);
        Element root = XMLUtil.getRoot(document);
        root.setTextContent(name);
        XMLUtil.saveXMLDocument(document, defaultTemplate);
    }

    public Object[] getTemplateNames() {
        return templates.keySet().toArray();
    }

    public Template getTemplate(Object key) {
        return templates.get(key);
    }

    public boolean templateExists(String name) {
        return templates.containsKey(name);
    }

    // 初始化模板
    private void initTemplates() {
        Document document = XMLUtil.loadXMLDocument(defaultTemplate);
        Element temp = XMLUtil.getRoot(document);
        defTemp = temp.getTextContent();

        document = XMLUtil.loadXMLDocument(templatesXML);
        Element root = XMLUtil.getRoot(document, XMLUtil.TEMPLATES_NODE);

        NodeList nodeList = root.getElementsByTagName(XMLUtil.TEMPLATE_NODE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Template template = new Template(XMLUtil.getNameAttr(element));

            Element chart = XMLUtil.getChartNode(element);
            String chartName = XMLUtil.getNameAttr(chart);
            template.setChart(ChartManager.getDefault().getChart(chartName));
            Element chartProperties = XMLUtil.getPropertiesNode(chart);
            template.getChartProperties().loadFromTemplate(chartProperties);

            Element overlays = XMLUtil.getOverlaysNode(element);
            NodeList overlaysList = overlays.getElementsByTagName(XMLUtil.OVERLAY_NODE);
            for (int j = 0; j < overlaysList.getLength(); j++) {
                Element overlayNode = (Element) overlaysList.item(j);
                String overlayName = XMLUtil.getNameAttr(overlayNode);
                Overlay overlay = OverlayManager.getDefault().getOverlay(overlayName).newInstance();
                if (overlay != null) {
                    Element overlayProperties = XMLUtil.getPropertiesNode(overlayNode);
                    overlay.loadFromTemplate(overlayProperties);
                    template.addOverlay(overlay);
                }
            }

            Element indicators = XMLUtil.getIndicatorsNode(element);
            NodeList indicatorsList = indicators.getElementsByTagName(XMLUtil.INDICATOR_NODE);
            for (int j = 0; j < indicatorsList.getLength(); j++) {
                Element indicatorNode = (Element) indicatorsList.item(j);
                String indicatorName = XMLUtil.getNameAttr(indicatorNode);
                Indicator indicator = IndicatorManager.getDefault().getIndicator(indicatorName).newInstance();
                if (indicator != null) {
                    Element indicatorProperties = XMLUtil.getPropertiesNode(indicatorNode);
                    indicator.loadFromTemplate(indicatorProperties);
                    template.addIndicator(indicator);
                }
            }

            templates.put(template.getName(), template);
        }
    }

    // 保存模板
    public void saveToTemplate(String name, ChartFrame chartFrame) {
        Document document = XMLUtil.loadXMLDocument(templatesXML);
        Element root = XMLUtil.getRoot(document, XMLUtil.TEMPLATES_NODE);

        // create the template node
        Element template = XMLUtil.addTemplateNode(document, root, name);
        // save template details
        Element chart = XMLUtil.addChartNode(document, template, chartFrame.getChartData().getChart());
        Element chartProperties = XMLUtil.addPropertiesNode(document, chart);
        chartFrame.getChartProperties().saveToTemplate(document, chartProperties);

        List<Overlay> overlays = chartFrame.getMainPanel().getSplitPanel().getChartPanel().getOverlays();
        if (!overlays.isEmpty()) {
            Element overlaysNode = XMLUtil.addOverlaysNode(document, template);
            for (Overlay overlay : overlays) {
                Element overlayNode = XMLUtil.addOverlayNode(document, overlaysNode, overlay);
                Element overlayProperties = XMLUtil.addPropertiesNode(document, overlayNode);
                overlay.saveToTemplate(document, overlayProperties);
            }
        }

        List<Indicator> indicators = chartFrame.getMainPanel().getSplitPanel().getIndicatorsPanel().getIndicatorsList();
        if (!indicators.isEmpty()) {
            Element indicatorsNode = XMLUtil.addIndicatorsNode(document, template);
            for (Indicator indicator : indicators) {
                Element indicatorNode = XMLUtil.addIndicatorNode(document, indicatorsNode, indicator);
                Element indicatorProperties = XMLUtil.addPropertiesNode(document, indicatorNode);
                indicator.saveToTemplate(document, indicatorProperties);
            }
        }

        // save changes
        XMLUtil.saveXMLDocument(document, templatesXML);

        templates.clear();
        initTemplates();
    }

    // 删除模板
    public void removeTemplate(String name) {
        Document document = XMLUtil.loadXMLDocument(templatesXML);
        Element root = XMLUtil.getRoot(document, XMLUtil.TEMPLATES_NODE);
        NodeList nodeList = root.getElementsByTagName(XMLUtil.TEMPLATE_NODE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            node.getAttributes().getNamedItem(XMLUtil.NAME_ATTR).getNodeValue();
            if (node.getAttributes().getNamedItem(XMLUtil.NAME_ATTR).getNodeValue().equals(name)) {
                root.removeChild(node);
                break;
            }
        }
        XMLUtil.saveXMLDocument(document, templatesXML);

        templates.clear();
        initTemplates();
    }

    // 获得图层
    public Overlay getOverlay(int index) {
        Overlay overlay = null;
        Document document = XMLUtil.loadXMLDocument(templatesXML);
        Element root = XMLUtil.getRoot(document, XMLUtil.TEMPLATES_NODE);

        NodeList nodeList = root.getElementsByTagName(XMLUtil.TEMPLATE_NODE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (element.getAttribute(XMLUtil.NAME_ATTR).equals(defTemp)) {
                Element overlays = XMLUtil.getOverlaysNode(element);
                NodeList overlaysList = overlays.getElementsByTagName(XMLUtil.OVERLAY_NODE);
                Element overlayNode = (Element) overlaysList.item(index);
                Element overlayProperties = XMLUtil.getPropertiesNode(overlayNode);
                overlay = OverlayManager.getDefault().getOverlay(
                        XMLUtil.getNameAttr(overlayNode)).newInstance();
                overlay.loadFromTemplate(overlayProperties);
            }
        }

        return overlay;
    }

    // 获得指标
    public Indicator getIndicator(int index) {
        Indicator indicator = null;
        Document document = XMLUtil.loadXMLDocument(templatesXML);
        Element root = XMLUtil.getRoot(document, XMLUtil.TEMPLATES_NODE);

        NodeList nodeList = root.getElementsByTagName(XMLUtil.TEMPLATE_NODE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (element.getAttribute(XMLUtil.NAME_ATTR).equals(defTemp)) {
                Element indicators = XMLUtil.getIndicatorsNode(element);
                NodeList indicatorsList = indicators.getElementsByTagName(XMLUtil.INDICATOR_NODE);
                Element indicatorNode = (Element) indicatorsList.item(index);
                Element indicatorProperties = XMLUtil.getPropertiesNode(indicatorNode);
                indicator = IndicatorManager.getDefault().getIndicator(
                        XMLUtil.getNameAttr(indicatorNode)).newInstance();
                indicator.loadFromTemplate(indicatorProperties);
            }
        }

        return indicator;
    }

}
