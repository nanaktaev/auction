package by.company.auction.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class CustomBeanUtils {

    private static String[] getNullPropertyNamesOfAnObject(Object source) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();

        for (java.beans.PropertyDescriptor pd : pds) {
            Object sourceValue = src.getPropertyValue(pd.getName());
            if (sourceValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyNotNullProperties(Object source, Object target) {

        BeanUtils.copyProperties(source, target, getNullPropertyNamesOfAnObject(source));

    }
}
