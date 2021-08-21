package platform;

import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class CodeRestController {

    private static final String TIME = "<span id=\"time_restriction\"> %s </span>";
    private static final String VIEWS = "<span id=\"views_restriction\"> %s </span>";

    @Autowired
    private PublicCodeRepository publicCodeRepository;
    @Autowired
    private PrivateCodeRepository privateCodeRepository;

    @GetMapping("/code/{id}")
    @Transactional
    public ModelAndView getCodeHtml(HttpServletResponse response, @PathVariable String id) {
        Utils.responseToHTML(response);
        ModelAndView modelAndView = new ModelAndView("view");
        Code code = getRecord(id);
        if (code == null) {
            return new ModelAndView("404", HttpStatus.NOT_FOUND);
        }

        modelAndView.addObject("codeBody", code.getCode());
        modelAndView.addObject("date", code.getDate()
                .replaceAll("-", "/")
                .replaceFirst("\\.[\\d]++", ""));


        if (code.getViews() > 0) {
            modelAndView.addObject("view", String.format(VIEWS, code.getViews() - 1));
        }
        if (code.getTime() > 0) {
            modelAndView.addObject("time", String.format(TIME, code.getTime()));
        }



        if (code.getClass() == PrivateCode.class) {
            if ((code.getViews() == 0 && code.getTime() == 0)) {
                if (privateCodeRepository.deleteByUuid(code.getUuid()).isPresent()) {
                    System.out.printf("%s deleted by views.\n", code.getUuid());
                    return getCodeHtml(response, id);
                }
            }
            privateCodeRepository.updateViews(((PrivateCode) code).getUuid(),
                    code.getViews() - 1);
        }


        return modelAndView;
    }

    private Code getRecord(String uuid) {
        Optional<PublicCode> publicCode = publicCodeRepository.findByUuid(uuid);
        if (publicCode.isPresent()) {
            return publicCode.get();
        } else {
            Optional<PrivateCode> privateCode = privateCodeRepository.findByUuid(uuid);
            if (privateCode.isPresent()) {
                return privateCode.get();
            }
        }
        return null;
    }

    @GetMapping("/code/new")
    public ModelAndView newCodeHtml(HttpServletResponse response) {
        Utils.responseToHTML(response);

        return new ModelAndView("new");
    }

    @GetMapping("/api/code/{id}")
    @Transactional
    public JSonForm getCodeJson(HttpServletResponse response, @PathVariable String id) {
        Utils.responseToJson(response);
        Code code = getRecord(id);
        try {
            code.getClass();
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException();
            //return "404 not found";
        }

        int view = code.getViews();
        if (view > 0) {
            --view;
        }

        if (code.getClass() == PrivateCode.class) {
            if ((code.getViews() == 0 && code.getTime() == 0)) {
                if (privateCodeRepository.deleteByUuid(code.getUuid()).isPresent()) {
                    System.out.printf("%s deleted by views.\n", code.getUuid());
                    return getCodeJson(response, id);
                }
            }
            privateCodeRepository.updateViews(((PrivateCode) code).getUuid(),
                    code.getViews() - 1);
        }

        return new JSonForm(code.getCode(), code.getDate(), code.getTime(), view);
    }


    @PostMapping(value = "/api/code/new", consumes = "application/json")
    public String newCodeJson(HttpServletResponse response, @RequestBody PublicCode code) {
        String uuid = getUUID();
        code.setUuid(uuid);
        code.setDate(getTime());
        if (code.getTime() > 0 || code.getViews() > 0) {
            System.out.println(code.getViews());
            privateCodeRepository.save(new PrivateCode(uuid,
                    code.getCode(), code.getTime(), code.getViews(), getTime()));
        } else {
            publicCodeRepository.save(code);
        }
        Utils.responseToJson(response);
        return String.format("{ \"id\" : \"%s\" }", uuid);
    }

    @GetMapping("/api/code/latest")
    public List<JSonForm> newCodeJson(HttpServletResponse response) {
        Utils.responseToJson(response);

        List<Code> codeList = getRecordList();
        List<JSonForm> printList = new ArrayList<>();
        int size = Math.min(codeList.size(), 10);
        for (int i = 0 ; i < size ; i++) {
            printList.add(new JSonForm(codeList.get(i).getCode(),
                    codeList.get(i).getDate(),
                    0, 0));
        }
        return printList;
    }

    private List<Code> getRecordList() {
        List<Code> arrayList = new ArrayList<>();
        publicCodeRepository.findAll().forEach(arrayList::add);
        Collections.reverse(arrayList);
        return arrayList;
    }

    @GetMapping("/code/latest")
    public String getLatestHtml(HttpServletResponse response) {
        Utils.responseToHTML(response);

        HashMap root = new HashMap();
        List<Code> codeList = getRecordList();
        List<String> printList = new ArrayList<>();
        int size = Math.min(codeList.size(), 10);

        for (int i = 0; i < size; i++) {
            printList.add(String.format(
                    "<span class=\"date\"> %s </span>\n" +
                            "<div>\n" +
                            "    <pre>\n" +
                            "    <code class=\"java\"> %s </code>\n" +
                            "    </pre>\n" +
                            "</div>",
                    codeList.get(i)
                            .getDate().replaceAll("-", "/")
                            .replaceFirst("\\.[\\d]++", ""),
                    codeList.get(i).getCode()));
        }

        root.put("users", printList);

        try {
            Template template = FreeMarkConf.configuration().getTemplate("latest.html");
            Writer out = new StringWriter();
            template.process(root, out);

            return out.toString();

        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

        return "Error";
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}

class FreeMarkConf {

    public static Configuration configuration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        try {
            cfg.setDirectoryForTemplateLoading(new File(
                    "F:\\Java\\Code Sharing Platform\\Code Sharing Platform\\task\\src\\resources\\templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return cfg;
    }

}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}



