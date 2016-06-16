<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:akt="http://www.parlament.gov.rs/akt"
     version="1.0">
    
    <xsl:template match="/akt:Akt">
        <html>
            <head>
               
            </head>
            
            <body style="font-family:Helvetica">
                <br/>
                <h1 align="center" style="font-size:16pt; margin-top:93pt" ><xsl:value-of select="akt:Naziv"/></h1>
             
                <xsl:apply-templates/>
              
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="akt:ID"></xsl:template>
    <xsl:template match="akt:Odbornik"></xsl:template>
    <xsl:template match="akt:Status"></xsl:template>
    <xsl:template match="akt:Naziv"></xsl:template>
    <xsl:template match="akt:Datum_predlaganja"></xsl:template>
    
    
    <xsl:template match="ds:Signature">
        
    </xsl:template>

    
    <xsl:template match="akt:Deo">
        
        <h2 align="center" style="font-size:12.2pt; margin-top:23pt"><xsl:value-of select="akt:Naziv"/></h2>
       
    </xsl:template>
    
    <!--
    <xsl:template match="Glava">
        
        <h2 align="center" style="font-size:12.2pt; margin-top:15pt"><xsl:value-of select="@redni_broj"/></h2>
        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="Naziv"/></h2>
        <xsl:apply-templates select="Odeljak"/>
        <xsl:apply-templates select="Clan"/>
    </xsl:template>
    
    <xsl:template match="Odeljak">
        
        
        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="Naziv"/></h2>
        <xsl:apply-templates select="Clan"/>
    </xsl:template>
    
    <xsl:template match="Clan">
        
        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="Naziv"/></h2>
        <h2 align="center" style="font-size:10.4pt"><xsl:value-of select="@redni_broj"/></h2>
        <xsl:apply-templates select="Stav"/>
        
    </xsl:template>
    
    <xsl:template match="Stav">
        <div align="center">
            <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt"><xsl:value-of select="Tekst"/></p>
            <xsl:apply-templates select="Tacka"/>
        </div>
        
    </xsl:template>
    
    <xsl:template match="Tacka">
     
        <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt">
            <span><xsl:value-of select="@redni_broj"/></span>
            <xsl:value-of select="Tekst"/>
        </p>
        <xsl:apply-templates select="Podtacka"/>
        
    </xsl:template>
    
    <xsl:template match="Podtacka">
        
        <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt">
            <span><xsl:value-of select="@redni_broj"/></span>
            <xsl:value-of select="Tekst"/>
        </p>
        
    </xsl:template>-->
   
    
</xsl:stylesheet>