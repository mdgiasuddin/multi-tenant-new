package org.example.multitenant.config.datasource;

//@Component
//@Order(1)
class TenantFilter {

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String tenantName = httpRequest.getHeader("X-TenantID");
//        TenantContext.setCurrentTenant(tenantName);
//
//        try {
//            chain.doFilter(request, response);
//        } finally {
//            TenantContext.setCurrentTenant("");
//        }
//    }
}
