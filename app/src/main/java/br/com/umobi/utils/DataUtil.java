package br.com.umobi.utils;

import android.annotation.SuppressLint;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DataUtil {
	public static String transformDateToSting(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date transformStringToDate(String format, String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}

    public static String formatDateToString(DateTime time){
        return new StringBuilder()
                .append(time.dayOfMonth().getAsText())
                .append(" de ")
                .append(time.monthOfYear().getAsText())
                .append(" de ")
                .append(time.year().getAsText()).toString();
    }
	
	public static double diferencaEmDias(Date dataInicial, Date dataFinal){
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        double diferencaEmDias = (diferenca /1000) / 60 / 60 /24;   
        long horasRestantes = (diferenca /1000) / 60 / 60 %24;   
        result = diferencaEmDias + (horasRestantes /24d);   
        return result;  
    }  
	
	public static String obterDiaDaSemana(int dia){
		String retorno;
		switch (dia) {
		case Calendar.SUNDAY:
			retorno = "Domingo";
			break;
		case Calendar.MONDAY:
			retorno = "Segunda-feira";
			break;
		case Calendar.TUESDAY:
			retorno = "Terça-feira";
			break;
		case Calendar.WEDNESDAY:
			retorno = "Quarta-feira";
			break;
		case Calendar.THURSDAY:
			retorno = "Quinta-feira";
			break;
		case Calendar.FRIDAY:
			retorno = "Sexta-feira";
			break;
		default:
			retorno = "Sábado";
			break;
		}
		return retorno;
	}


    public static double diferencaEmHoras(Date dataInicial, Date dataFinal){
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        long diferencaEmHoras = (diferenca /1000) / 60 / 60;  
        long minutosRestantes = (diferenca / 1000)/60 %60;  
        double horasRestantes = minutosRestantes / 60d;  
        result = diferencaEmHoras + (horasRestantes);  
        return result;  
    }  
      
    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal){
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        double diferencaEmMinutos = (diferenca /1000) / 60;   
        long segundosRestantes = (diferenca / 1000)%60; 
        result = diferencaEmMinutos + (segundosRestantes /60d); 
        return result;  
    }  
}
