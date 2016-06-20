<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:akt="http://www.parlament.gov.rs/akt" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">
    
    <xsl:template match="/akt:Akt">
        <fo:root font-family="Helvetica" margin-right="80pt" margin-left="80pt">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="akt-page" margin-bottom="80pt" margin-top="80pt">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="akt-page">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block text-align="center" font-size="16pt" font-weight="bold">
                        <xsl:value-of select="akt:Naziv"></xsl:value-of>
                    </fo:block>
                   <xsl:apply-templates></xsl:apply-templates>
                       
                  
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    
    <xsl:template match="akt:Deo">
         
        <fo:block text-align="center" font-size="12.2pt" margin-top="23pt" font-weight="bold">
            <xsl:value-of select="akt:Naziv"/>
        </fo:block>
        <xsl:apply-templates select="akt:Glava"></xsl:apply-templates>
        
    </xsl:template>
    
    
    <xsl:template match="akt:Glava">
        
        <fo:block>
            
            <fo:block text-align="center" font-size="12.2pt" margin-top="15pt" font-weight="bold"><xsl:value-of select="@redni_broj"/></fo:block>
            <fo:block text-align="center" font-size="11.3pt" font-weight="bold" margin-top="12pt"><xsl:value-of select="akt:Naziv"/></fo:block>
            <xsl:apply-templates select="akt:Odeljak"/>
            <xsl:apply-templates select="akt:Clan"/>
            
            
        </fo:block>
        
        
    </xsl:template>
    
    <xsl:template match="akt:Odeljak">
        <fo:block text-align="center" font-size="11.3pt" font-weight="bold" margin-top="12pt"><xsl:value-of select="akt:Naziv"/></fo:block>
        <xsl:apply-templates select="akt:Clan"/>
    </xsl:template>
    
    
    <xsl:template match="akt:Clan">
        
        <fo:block text-align="center" font-size="11.3pt" font-weight="bold" margin-top="12pt"><xsl:value-of select="akt:Naziv"/></fo:block>
        <fo:block text-align="center"  font-size="10.4pt" font-weight="bold" margin-top="12pt">Član <xsl:value-of select="@redni_broj"/>.</fo:block>
        
            <xsl:apply-templates select="akt:Stav"/>
        
       
        
    </xsl:template>
    
    <xsl:template match="akt:Stav">
        <fo:block text-align="center">
            <fo:block text-align="justify" width="400pt" text-indent="60pt" font-size="10.4pt" margin-top="12pt"><xsl:value-of select="akt:Tekst"/></fo:block>
            <xsl:apply-templates select="akt:Tacka"/>
        </fo:block>
        
    </xsl:template>
    <xsl:template match="akt:Tacka">
        
        <fo:block text-align="justify" width="400pt" text-indent="60pt" font-size="10.4pt" margin-top="12pt">
            <fo:inline>(<xsl:value-of select="@redni_broj"/>)</fo:inline>
            <xsl:value-of select="akt:Tekst"/>
        </fo:block>
        <xsl:apply-templates select="akt:Podtacka"/>
        
    </xsl:template>
    
    <xsl:template match="akt:Podtacka">
        
        <fo:block  text-align="justify" width="400pt" text-indent="60pt" font-size="10.4pt" margin-top="12pt">
            <fo:inline><xsl:value-of select="@redni_broj"/>)</fo:inline>
            <xsl:value-of select="akt:Tekst"/>
        </fo:block>
        
    </xsl:template>
    
</xsl:stylesheet>
